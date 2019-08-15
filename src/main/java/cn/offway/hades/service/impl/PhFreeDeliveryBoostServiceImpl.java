package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhFreeDeliveryBoostService;

import cn.offway.hades.domain.PhFreeDeliveryBoost;
import cn.offway.hades.repository.PhFreeDeliveryBoostRepository;


/**
 * 免费送助力Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
@Service
public class PhFreeDeliveryBoostServiceImpl implements PhFreeDeliveryBoostService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhFreeDeliveryBoostRepository phFreeDeliveryBoostRepository;
	
	@Override
	public PhFreeDeliveryBoost save(PhFreeDeliveryBoost phFreeDeliveryBoost){
		return phFreeDeliveryBoostRepository.save(phFreeDeliveryBoost);
	}
	
	@Override
	public PhFreeDeliveryBoost findOne(Long id){
		return phFreeDeliveryBoostRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phFreeDeliveryBoostRepository.delete(id);
	}

	@Override
	public List<PhFreeDeliveryBoost> save(List<PhFreeDeliveryBoost> entities){
		return phFreeDeliveryBoostRepository.save(entities);
	}
}
