package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhOrderExpressInfoService;

import cn.offway.hades.domain.PhOrderExpressInfo;
import cn.offway.hades.repository.PhOrderExpressInfoRepository;


/**
 * 订单物流Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhOrderExpressInfoServiceImpl implements PhOrderExpressInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhOrderExpressInfoRepository phOrderExpressInfoRepository;
	
	@Override
	public PhOrderExpressInfo save(PhOrderExpressInfo phOrderExpressInfo){
		return phOrderExpressInfoRepository.save(phOrderExpressInfo);
	}
	
	@Override
	public PhOrderExpressInfo findOne(Long id){
		return phOrderExpressInfoRepository.findOne(id);
	}
}
