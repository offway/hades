package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhRole;
import cn.offway.hades.repository.PhRoleRepository;
import cn.offway.hades.service.PhRoleService;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhRoleServiceImpl implements PhRoleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhRoleRepository phRoleRepository;
	
	@Override
	public PhRole save(PhRole phRole){
		return phRoleRepository.save(phRole);
	}
	
	@Override
	public PhRole findOne(Long id){
		return phRoleRepository.findOne(id);
	}
}
