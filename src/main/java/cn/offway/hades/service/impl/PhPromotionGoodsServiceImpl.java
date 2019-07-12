package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhPromotionGoodsService;

import cn.offway.hades.domain.PhPromotionGoods;
import cn.offway.hades.repository.PhPromotionGoodsRepository;


/**
 * 促销活动对应商品Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPromotionGoodsServiceImpl implements PhPromotionGoodsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhPromotionGoodsRepository phPromotionGoodsRepository;
	
	@Override
	public PhPromotionGoods save(PhPromotionGoods phPromotionGoods){
		return phPromotionGoodsRepository.save(phPromotionGoods);
	}
	
	@Override
	public PhPromotionGoods findOne(Long id){
		return phPromotionGoodsRepository.findOne(id);
	}
}
