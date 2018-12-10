package cn.offway.hades.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhProductInfo;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.repository.PhProductInfoRepository;
import cn.offway.hades.service.PhProductInfoService;
import cn.offway.hades.service.QiniuService;


/**
 * 活动产品表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhProductInfoServiceImpl implements PhProductInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhProductInfoRepository phProductInfoRepository;
	
	@Autowired
	private QiniuService qiniuService;
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@Value("${ph.file.path}")
	private String FILE_PATH;
	
	@Override
	public PhProductInfo save(PhProductInfo phProductInfo){
		return phProductInfoRepository.save(phProductInfo);
	}
	
	@Override
	public PhProductInfo saveProduct(PhProductInfo phProductInfo){
		Long productId = phProductInfo.getId();
		boolean isAdd = true;
		if(null!=productId){
			isAdd = false;
			PhProductInfo productInfo = findOne(productId);
			String image = productInfo.getImage();
			if(null!= image &&!image.equals(phProductInfo.getImage())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(image.replace(qiniuProperties.getUrl()+"/", ""));
			}
			String banner = productInfo.getBanner();
			if(null!= banner &&!banner.equals(phProductInfo.getBanner())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(banner.replace(qiniuProperties.getUrl()+"/", ""));
			}
			String shareImage = productInfo.getShareImage();
			if(null!= shareImage &&!shareImage.equals(phProductInfo.getShareImage())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(shareImage.replace(qiniuProperties.getUrl()+"/", ""));
			}
			String showImage = productInfo.getShowImage();
			if(null!= showImage &&!showImage.equals(phProductInfo.getShowImage())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(showImage.replace(qiniuProperties.getUrl()+"/", ""));
			}
			String saveImage = productInfo.getSaveImage();
			if(null!= saveImage &&!saveImage.equals(phProductInfo.getSaveImage())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(saveImage.replace(qiniuProperties.getUrl()+"/", ""));
			}
			
			String background = productInfo.getBackground();
			if(null!= background &&!background.equals(phProductInfo.getBackground())){
				//如果资源变动则删除本地资源
				File file=new File(FILE_PATH+"/"+StringUtils.substringAfterLast(background, "/"));
		        if(file.exists()&&file.isFile()){
		        	file.delete();
		        }
			}
			phProductInfo.setCreateTime(productInfo.getCreateTime());
			phProductInfo.setRuleContent(productInfo.getRuleContent());
		}
		//设置缩略图为活动列表图
		phProductInfo.setThumbnail(phProductInfo.getImage());
		if(isAdd){
			phProductInfo.setCreateTime(new Date());
		}
		
		phProductInfo = save(phProductInfo);
		
		if(isAdd){
			//设置抽奖码自增1
			phProductInfoRepository.sequence(phProductInfo.getId());
		}
		
		return phProductInfo;
	}
	
	@Override
	public PhProductInfo findOne(Long id){
		return phProductInfoRepository.findOne(id);
	}
	
	@Override
	public List<PhProductInfo> findByEndTime(Date endTime){
		return phProductInfoRepository.findByEndTime(endTime);
	}
	
	@Override
	public int updateSort(Long productId){
		return phProductInfoRepository.updateSort(productId);
	}
	
	@Override
	public Page<PhProductInfo> findByPage(final String name,Pageable page){
		return phProductInfoRepository.findAll(new Specification<PhProductInfo>() {
			
			@Override
			public Predicate toPredicate(Root<PhProductInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
	public Page<PhProductInfo> findByType(final String type,Pageable page){
		return phProductInfoRepository.findAll(new Specification<PhProductInfo>() {
			
			@Override
			public Predicate toPredicate(Root<PhProductInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				Date now = new Date();
				if(StringUtils.isNotBlank(type)){
					if("0".equals(type)){
						//进行中
						params.add(criteriaBuilder.lessThan(root.get("beginTime"), now));
						params.add(criteriaBuilder.isNull(root.get("video")));
						Predicate[] predicates = new Predicate[params.size()];
		                criteriaQuery.where(params.toArray(predicates));
		                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sort")),criteriaBuilder.desc(root.get("beginTime")));
					}else if("1".equals(type)){
						//未开始
						params.add(criteriaBuilder.greaterThanOrEqualTo(root.get("beginTime"), now));
						Predicate[] predicates = new Predicate[params.size()];
		                criteriaQuery.where(params.toArray(predicates));
		                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sort")),criteriaBuilder.asc(root.get("beginTime")));
					
					}else if("2".equals(type)){
						//已结束
						params.add(criteriaBuilder.lessThan(root.get("endTime"), now));
						params.add(criteriaBuilder.isNotNull(root.get("video")));
						Predicate[] predicates = new Predicate[params.size()];
		                criteriaQuery.where(params.toArray(predicates));
		                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sort")),criteriaBuilder.desc(root.get("endTime")));
					}
					
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				
				return null;
			}
		}, page);
	}
}
