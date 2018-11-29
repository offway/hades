package cn.offway.hades.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhActivityImage;
import cn.offway.hades.domain.PhActivityInfo;
import cn.offway.hades.domain.PhProductInfo;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.repository.PhActivityInfoRepository;
import cn.offway.hades.service.PhActivityImageService;
import cn.offway.hades.service.PhActivityInfoService;
import cn.offway.hades.service.PhActivityJoinService;
import cn.offway.hades.service.QiniuService;


/**
 * 活动表-每日福利Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhActivityInfoServiceImpl implements PhActivityInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhActivityInfoRepository phActivityInfoRepository;
	
	@Autowired
	private PhActivityImageService phActivityImageService;
	
	@Autowired
	private PhActivityJoinService phActivityJoinService;
	
	@Autowired
	private QiniuService qiniuService;
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@Override
	public PhActivityInfo save(PhActivityInfo phActivityInfo){
		return phActivityInfoRepository.save(phActivityInfo);
	}
	
	@Override
	public PhActivityInfo findOne(Long id){
		return phActivityInfoRepository.findOne(id);
	}
	
	@Override
	public List<PhActivityInfo> findByEndTime(Date endtime){
		return phActivityInfoRepository.findByEndTime(endtime);
	}
	
	@Override
	public Page<PhActivityInfo> findByPage(final String name,Pageable page){
		return phActivityInfoRepository.findAll(new Specification<PhActivityInfo>() {
			
			@Override
			public Predicate toPredicate(Root<PhActivityInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(name)){
					params.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		}, page);
	}
	
	@Override
	public Map<String, List<PhActivityInfo>> list(){
		Map<String, List<PhActivityInfo>> resultMap = new HashMap<>();
		resultMap.put("current", phActivityInfoRepository.findByNow());
		resultMap.put("before", phActivityInfoRepository.findByNow());
		return resultMap;
	}
	
	@Override
	public Map<String, Object> detail(Long activityId,String unionid){
		Map<String, Object> resultMap = new HashMap<>();
		PhActivityInfo phActivityInfo = findOne(activityId);
		resultMap.put("activity", phActivityInfo);
		//查询活动图片
		List<PhActivityImage> images = phActivityImageService.findByActivityId(activityId);
		Map<String, List<String>> img = new HashMap<>();
		for (PhActivityImage phActivityImage : images) {
			String key = phActivityImage.getType();
			key = "0".equals(key)?"banner":"detail";
			List<String> urls = img.get(key);
			if(null == urls ){
				urls = new ArrayList<>();
			}
			urls.add(phActivityImage.getImageUrl());
			img.put(key,urls);
		}
		resultMap.putAll(img);
		
		int count = phActivityJoinService.countByUnionidAndActivityId(unionid, activityId);
		resultMap.put("isJoin", count>0?true:false);
		
		return resultMap;
	}
	
	@Override
	public void save(PhActivityInfo phActivityInfo,String banner,String detail){
		Long productId = phActivityInfo.getId();
		if(null!=productId){
			PhActivityInfo productInfo = findOne(productId);
			String image = productInfo.getImage();
			if(!image.equals(phActivityInfo.getImage())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(image.replace(qiniuProperties.getUrl()+"/", ""));
			}
			phActivityInfo.setCreateTime(productInfo.getCreateTime());
			phActivityInfo.setStatus(productInfo.getStatus());
		}
		phActivityInfo = save(phActivityInfo);
		
		List<String> banners = Arrays.asList(banner.split("#"));
		List<String> details = Arrays.asList(detail.split("#"));
		List<PhActivityImage> activityImages = phActivityImageService.findByActivityId(productId);
		for (PhActivityImage phActivityImage : activityImages) {
			String image = phActivityImage.getImageUrl();
			if(phActivityImage.getType().equals("0") && (!banners.contains(image))){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(image.replace(qiniuProperties.getUrl()+"/", ""));
			}
			if(phActivityImage.getType().equals("1") && (!details.contains(image))){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(image.replace(qiniuProperties.getUrl()+"/", ""));
			}
		}
		
		phActivityImageService.delete(activityImages);
		List<PhActivityImage> images = new ArrayList<>();
		Date now = new Date();
		for (String b : banners) {
			if(StringUtils.isNotBlank(b)){
				PhActivityImage activityImage = new PhActivityImage();
				activityImage.setActivityId(phActivityInfo.getId());
				activityImage.setActivityName(phActivityInfo.getName());
				activityImage.setCreateTime(now);
				activityImage.setImageUrl(b);
				activityImage.setSort(0L);
				activityImage.setType("0");
				images.add(activityImage);
			}
		}
		
		for (String b : details) {
			if(StringUtils.isNotBlank(b)){
				PhActivityImage activityImage = new PhActivityImage();
				activityImage.setActivityId(phActivityInfo.getId());
				activityImage.setActivityName(phActivityInfo.getName());
				activityImage.setCreateTime(now);
				activityImage.setImageUrl(b);
				activityImage.setSort(0L);
				activityImage.setType("1");
				images.add(activityImage);
			}
		}
		phActivityImageService.save(images);
		
		
	}
	
	@Override
	public boolean imagesDelete(Long activityImageId){
		//TODO imagesDelete
		PhActivityImage activityImage = phActivityImageService.findOne(activityImageId);
		activityImage.getImageUrl();
		return true;
	}
	
}
