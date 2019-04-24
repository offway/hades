package cn.offway.hades.service;

import cn.offway.hades.domain.PhSmsInfo;

/**
 * 短信记录Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhSmsInfoService{

	PhSmsInfo save(PhSmsInfo phSmsInfo);
	
	PhSmsInfo findOne(Long id);
}
