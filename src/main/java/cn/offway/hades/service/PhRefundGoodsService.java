package cn.offway.hades.service;

import cn.offway.hades.domain.PhRefundGoods;

/**
 * 退款/退货商品明细Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhRefundGoodsService{

	PhRefundGoods save(PhRefundGoods phRefundGoods);
	
	PhRefundGoods findOne(Long id);
}
