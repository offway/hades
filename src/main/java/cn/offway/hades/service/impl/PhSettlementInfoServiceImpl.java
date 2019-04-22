package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhSettlementInfoService;

import cn.offway.hades.domain.PhSettlementInfo;
import cn.offway.hades.repository.PhSettlementInfoRepository;


/**
 * 商户结算表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhSettlementInfoServiceImpl implements PhSettlementInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhSettlementInfoRepository phSettlementInfoRepository;
	
	@Override
	public PhSettlementInfo save(PhSettlementInfo phSettlementInfo){
		return phSettlementInfoRepository.save(phSettlementInfo);
	}
	
	@Override
	public PhSettlementInfo findOne(Long id){
		return phSettlementInfoRepository.findOne(id);
	}
}
