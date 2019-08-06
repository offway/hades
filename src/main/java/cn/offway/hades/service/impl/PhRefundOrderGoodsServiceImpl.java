package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhRefundOrderGoodsService;

import cn.offway.hades.domain.PhRefundOrderGoods;
import cn.offway.hades.repository.PhRefundOrderGoodsRepository;


/**
 * 退换货后的订单商品Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-06 15:46:21 Exp $
 */
@Service
public class PhRefundOrderGoodsServiceImpl implements PhRefundOrderGoodsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhRefundOrderGoodsRepository phRefundOrderGoodsRepository;
	
	@Override
	public PhRefundOrderGoods save(PhRefundOrderGoods phRefundOrderGoods){
		return phRefundOrderGoodsRepository.save(phRefundOrderGoods);
	}
	
	@Override
	public PhRefundOrderGoods findOne(Long id){
		return phRefundOrderGoodsRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phRefundOrderGoodsRepository.delete(id);
	}

	@Override
	public List<PhRefundOrderGoods> save(List<PhRefundOrderGoods> entities){
		return phRefundOrderGoodsRepository.save(entities);
	}
}
