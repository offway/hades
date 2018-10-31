package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhProductInfo;
import cn.offway.hades.repository.PhProductInfoRepository;
import cn.offway.hades.service.PhProductInfoService;


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
	
	@Override
	public PhProductInfo save(PhProductInfo phProductInfo){
		return phProductInfoRepository.save(phProductInfo);
	}
	
	@Override
	public PhProductInfo findOne(Long id){
		return phProductInfoRepository.findOne(id);
	}
}
