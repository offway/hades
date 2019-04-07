package cn.offway.hades.service;

import cn.offway.hades.domain.PhVoucherProject;

/**
 * 优惠券方案Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhVoucherProjectService{

	PhVoucherProject save(PhVoucherProject phVoucherProject);
	
	PhVoucherProject findOne(Long id);
}
