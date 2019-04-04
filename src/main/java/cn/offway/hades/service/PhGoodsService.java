package cn.offway.hades.service;

import cn.offway.hades.domain.PhGoods;

/**
 * 商品表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsService{

	PhGoods save(PhGoods phGoods);
	
	PhGoods findOne(Long id);
}
