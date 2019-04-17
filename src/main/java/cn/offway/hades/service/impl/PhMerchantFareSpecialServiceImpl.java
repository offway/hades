package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhMerchantFareSpecialService;

import cn.offway.hades.domain.PhMerchantFareSpecial;
import cn.offway.hades.repository.PhMerchantFareSpecialRepository;


/**
 * 商户运费特殊表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhMerchantFareSpecialServiceImpl implements PhMerchantFareSpecialService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhMerchantFareSpecialRepository phMerchantFareSpecialRepository;
	
	@Override
	public PhMerchantFareSpecial save(PhMerchantFareSpecial phMerchantFareSpecial){
		return phMerchantFareSpecialRepository.save(phMerchantFareSpecial);
	}
	
	@Override
	public PhMerchantFareSpecial findOne(Long id){
		return phMerchantFareSpecialRepository.findOne(id);
	}
}
