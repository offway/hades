package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhGoodsService;

import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.repository.PhGoodsRepository;


/**
 * 商品表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsServiceImpl implements PhGoodsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsRepository phGoodsRepository;
	
	@Override
	public PhGoods save(PhGoods phGoods){
		return phGoodsRepository.save(phGoods);
	}
	
	@Override
	public PhGoods findOne(Long id){
		return phGoodsRepository.findOne(id);
	}
}
