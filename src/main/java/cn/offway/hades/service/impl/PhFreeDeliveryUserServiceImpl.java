package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhFreeDeliveryUserService;

import cn.offway.hades.domain.PhFreeDeliveryUser;
import cn.offway.hades.repository.PhFreeDeliveryUserRepository;


/**
 * 免费送参与用户Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
@Service
public class PhFreeDeliveryUserServiceImpl implements PhFreeDeliveryUserService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhFreeDeliveryUserRepository phFreeDeliveryUserRepository;
	
	@Override
	public PhFreeDeliveryUser save(PhFreeDeliveryUser phFreeDeliveryUser){
		return phFreeDeliveryUserRepository.save(phFreeDeliveryUser);
	}
	
	@Override
	public PhFreeDeliveryUser findOne(Long id){
		return phFreeDeliveryUserRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phFreeDeliveryUserRepository.delete(id);
	}

	@Override
	public List<PhFreeDeliveryUser> save(List<PhFreeDeliveryUser> entities){
		return phFreeDeliveryUserRepository.save(entities);
	}
}
