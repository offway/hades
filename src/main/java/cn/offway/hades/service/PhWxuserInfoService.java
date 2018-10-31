package cn.offway.hades.service;

import cn.offway.hades.domain.PhWxuserInfo;

/**
 * 微信用户信息Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhWxuserInfoService{

	PhWxuserInfo save(PhWxuserInfo phWxuserInfo);
	
	PhWxuserInfo findOne(Long id);
}
