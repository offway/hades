package cn.offway.hades.service;

import cn.offway.hades.domain.PhPreorderInfo;


/**
 * 预生成订单Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhPreorderInfoService{

	PhPreorderInfo save(PhPreorderInfo phPreorderInfo);
	
	PhPreorderInfo findOne(Long id);

	PhPreorderInfo findByOrderNoAndStatus(String orderno, String status);

	int countByUserIdAndStatus(Long userId, String status);
}
