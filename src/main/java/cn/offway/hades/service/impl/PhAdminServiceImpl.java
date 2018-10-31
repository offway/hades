package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.repository.PhAdminRepository;
import cn.offway.hades.service.PhAdminService;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhAdminServiceImpl implements PhAdminService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhAdminRepository phAdminRepository;
	
	@Override
	public PhAdmin save(PhAdmin phAdmin){
		return phAdminRepository.save(phAdmin);
	}
	
	@Override
	public PhAdmin findOne(Long id){
		return phAdminRepository.findOne(id);
	}
}
