package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhRefundGoodsService;

import cn.offway.hades.domain.PhRefundGoods;
import cn.offway.hades.repository.PhRefundGoodsRepository;


/**
 * 退款/退货商品明细Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhRefundGoodsServiceImpl implements PhRefundGoodsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhRefundGoodsRepository phRefundGoodsRepository;
	
	@Override
	public PhRefundGoods save(PhRefundGoods phRefundGoods){
		return phRefundGoodsRepository.save(phRefundGoods);
	}
	
	@Override
	public PhRefundGoods findOne(Long id){
		return phRefundGoodsRepository.findOne(id);
	}
}
