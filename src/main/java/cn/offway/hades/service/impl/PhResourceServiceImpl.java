package cn.offway.hades.service.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhResource;
import cn.offway.hades.repository.PhResourceRepository;
import cn.offway.hades.service.PhResourceService;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhResourceServiceImpl implements PhResourceService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhResourceRepository phResourceRepository;
	
	@Override
	public PhResource save(PhResource phResource){
		return phResourceRepository.save(phResource);
	}
	
	@Override
	public PhResource findOne(Long id){
		return phResourceRepository.findOne(id);
	}
	
	@Override
	public Set<String> findUrlsByAdminId(Long adminId){
		return phResourceRepository.findUrlsByAdminId(adminId);
	}
	
	@Override
	public List<PhResource> findByAdminId(Long adminId){
		return phResourceRepository.findByAdminId(adminId);
	}
}
