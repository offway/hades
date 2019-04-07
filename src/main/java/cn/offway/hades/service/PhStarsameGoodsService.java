package cn.offway.hades.service;

import cn.offway.hades.domain.PhStarsameGoods;

/**
 * 明星同款商品Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhStarsameGoodsService{

	PhStarsameGoods save(PhStarsameGoods phStarsameGoods);
	
	PhStarsameGoods findOne(Long id);
}
