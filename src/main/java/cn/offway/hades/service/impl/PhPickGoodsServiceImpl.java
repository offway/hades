package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhPickGoodsService;

import cn.offway.hades.domain.PhPickGoods;
import cn.offway.hades.repository.PhPickGoodsRepository;


/**
 * OFFWAY优选商品明细Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPickGoodsServiceImpl implements PhPickGoodsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhPickGoodsRepository phPickGoodsRepository;
	
	@Override
	public PhPickGoods save(PhPickGoods phPickGoods){
		return phPickGoodsRepository.save(phPickGoods);
	}
	
	@Override
	public PhPickGoods findOne(Long id){
		return phPickGoodsRepository.findOne(id);
	}
}
