package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhMerchantService;

import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.repository.PhMerchantRepository;


/**
 * 商户表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhMerchantServiceImpl implements PhMerchantService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhMerchantRepository phMerchantRepository;
	
	@Override
	public PhMerchant save(PhMerchant phMerchant){
		return phMerchantRepository.save(phMerchant);
	}
	
	@Override
	public PhMerchant findOne(Long id){
		return phMerchantRepository.findOne(id);
	}
}
