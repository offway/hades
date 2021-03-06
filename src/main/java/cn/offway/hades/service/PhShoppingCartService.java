package cn.offway.hades.service;

import cn.offway.hades.domain.PhShoppingCart;

/**
 * 购物车Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhShoppingCartService{

	PhShoppingCart save(PhShoppingCart phShoppingCart);
	
	PhShoppingCart findOne(Long id);
}
