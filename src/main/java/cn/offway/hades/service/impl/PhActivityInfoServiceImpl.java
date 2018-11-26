package cn.offway.hades.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhActivityImage;
import cn.offway.hades.domain.PhActivityInfo;
import cn.offway.hades.repository.PhActivityInfoRepository;
import cn.offway.hades.service.PhActivityImageService;
import cn.offway.hades.service.PhActivityInfoService;
import cn.offway.hades.service.PhActivityJoinService;


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
	
}
