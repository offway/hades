package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhPromotionInfoService;

import cn.offway.hades.domain.PhPromotionInfo;
import cn.offway.hades.repository.PhPromotionInfoRepository;


/**
 * 促销活动Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPromotionInfoServiceImpl implements PhPromotionInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhPromotionInfoRepository phPromotionInfoRepository;
	
	@Override
	public PhPromotionInfo save(PhPromotionInfo phPromotionInfo){
		return phPromotionInfoRepository.save(phPromotionInfo);
	}
	
	@Override
	public PhPromotionInfo findOne(Long id){
		return phPromotionInfoRepository.findOne(id);
	}
}
