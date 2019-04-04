package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhOrderGoodsService;

import cn.offway.hades.domain.PhOrderGoods;
import cn.offway.hades.repository.PhOrderGoodsRepository;


/**
 * 订单商品Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhOrderGoodsServiceImpl implements PhOrderGoodsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhOrderGoodsRepository phOrderGoodsRepository;
	
	@Override
	public PhOrderGoods save(PhOrderGoods phOrderGoods){
		return phOrderGoodsRepository.save(phOrderGoods);
	}
	
	@Override
	public PhOrderGoods findOne(Long id){
		return phOrderGoodsRepository.findOne(id);
	}
}
