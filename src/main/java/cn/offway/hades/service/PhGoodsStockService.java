package cn.offway.hades.service;

import cn.offway.hades.domain.PhGoodsStock;

/**
 * 商品库存Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsStockService{

	PhGoodsStock save(PhGoodsStock phGoodsStock);
	
	PhGoodsStock findOne(Long id);
}
