package cn.offway.hades.service;

import cn.offway.hades.domain.PhRefund;

/**
 * 退款/退货Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhRefundService{

	PhRefund save(PhRefund phRefund);
	
	PhRefund findOne(Long id);
}
