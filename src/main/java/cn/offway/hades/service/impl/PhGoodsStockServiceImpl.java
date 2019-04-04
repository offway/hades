package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhGoodsStockService;

import cn.offway.hades.domain.PhGoodsStock;
import cn.offway.hades.repository.PhGoodsStockRepository;


/**
 * 商品库存Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsStockServiceImpl implements PhGoodsStockService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsStockRepository phGoodsStockRepository;
	
	@Override
	public PhGoodsStock save(PhGoodsStock phGoodsStock){
		return phGoodsStockRepository.save(phGoodsStock);
	}
	
	@Override
	public PhGoodsStock findOne(Long id){
		return phGoodsStockRepository.findOne(id);
	}
}
