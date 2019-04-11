package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhGoodsPropertyService;

import cn.offway.hades.domain.PhGoodsProperty;
import cn.offway.hades.repository.PhGoodsPropertyRepository;


/**
 * 商品属性Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsPropertyServiceImpl implements PhGoodsPropertyService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsPropertyRepository phGoodsPropertyRepository;
	
	@Override
	public PhGoodsProperty save(PhGoodsProperty phGoodsProperty){
		return phGoodsPropertyRepository.save(phGoodsProperty);
	}
	
	@Override
	public PhGoodsProperty findOne(Long id){
		return phGoodsPropertyRepository.findOne(id);
	}
}
