package cn.offway.hades.service;

import cn.offway.hades.domain.PhGoodsType;

/**
 * 商品类别Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsTypeService{

	PhGoodsType save(PhGoodsType phGoodsType);
	
	PhGoodsType findOne(Long id);
}
