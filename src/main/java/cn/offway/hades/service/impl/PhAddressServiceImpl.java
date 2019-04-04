package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhAddressService;

import cn.offway.hades.domain.PhAddress;
import cn.offway.hades.repository.PhAddressRepository;


/**
 * 地址管理Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhAddressServiceImpl implements PhAddressService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhAddressRepository phAddressRepository;
	
	@Override
	public PhAddress save(PhAddress phAddress){
		return phAddressRepository.save(phAddress);
	}
	
	@Override
	public PhAddress findOne(Long id){
		return phAddressRepository.findOne(id);
	}
}
