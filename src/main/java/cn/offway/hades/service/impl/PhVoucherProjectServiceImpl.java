package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhVoucherProjectService;

import cn.offway.hades.domain.PhVoucherProject;
import cn.offway.hades.repository.PhVoucherProjectRepository;


/**
 * 优惠券方案Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhVoucherProjectServiceImpl implements PhVoucherProjectService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhVoucherProjectRepository phVoucherProjectRepository;
	
	@Override
	public PhVoucherProject save(PhVoucherProject phVoucherProject){
		return phVoucherProjectRepository.save(phVoucherProject);
	}
	
	@Override
	public PhVoucherProject findOne(Long id){
		return phVoucherProjectRepository.findOne(id);
	}
}
