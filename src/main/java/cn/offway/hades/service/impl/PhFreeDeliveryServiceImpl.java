package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhFreeDeliveryService;

import cn.offway.hades.domain.PhFreeDelivery;
import cn.offway.hades.repository.PhFreeDeliveryRepository;


/**
 * 免费送Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
@Service
public class PhFreeDeliveryServiceImpl implements PhFreeDeliveryService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhFreeDeliveryRepository phFreeDeliveryRepository;
	
	@Override
	public PhFreeDelivery save(PhFreeDelivery phFreeDelivery){
		return phFreeDeliveryRepository.save(phFreeDelivery);
	}
	
	@Override
	public PhFreeDelivery findOne(Long id){
		return phFreeDeliveryRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phFreeDeliveryRepository.delete(id);
	}

	@Override
	public List<PhFreeDelivery> save(List<PhFreeDelivery> entities){
		return phFreeDeliveryRepository.save(entities);
	}
}
