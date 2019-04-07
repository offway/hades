package cn.offway.hades.service;

import cn.offway.hades.domain.PhGoodsCategory;

/**
 * 商品类目Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsCategoryService{

	PhGoodsCategory save(PhGoodsCategory phGoodsCategory);
	
	PhGoodsCategory findOne(Long id);
}
