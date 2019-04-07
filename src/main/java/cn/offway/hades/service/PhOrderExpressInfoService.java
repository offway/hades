package cn.offway.hades.service;

import cn.offway.hades.domain.PhOrderExpressInfo;

/**
 * 订单物流Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhOrderExpressInfoService{

	PhOrderExpressInfo save(PhOrderExpressInfo phOrderExpressInfo);
	
	PhOrderExpressInfo findOne(Long id);
}
