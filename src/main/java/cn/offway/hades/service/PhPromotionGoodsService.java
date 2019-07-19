package cn.offway.hades.service;

import cn.offway.hades.domain.PhPromotionGoods;
import cn.offway.hades.domain.PhPromotionInfo;

import java.util.List;

/**
 * 促销活动对应商品Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhPromotionGoodsService{

	PhPromotionGoods save(PhPromotionGoods phPromotionGoods);
	
	PhPromotionGoods findOne(Long id);

	void del(Long id);

	List<PhPromotionGoods> findAllByPid(Long id);
}
