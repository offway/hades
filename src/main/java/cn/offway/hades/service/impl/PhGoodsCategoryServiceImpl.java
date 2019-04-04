package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhGoodsCategoryService;

import cn.offway.hades.domain.PhGoodsCategory;
import cn.offway.hades.repository.PhGoodsCategoryRepository;


/**
 * 商品类目Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsCategoryServiceImpl implements PhGoodsCategoryService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsCategoryRepository phGoodsCategoryRepository;
	
	@Override
	public PhGoodsCategory save(PhGoodsCategory phGoodsCategory){
		return phGoodsCategoryRepository.save(phGoodsCategory);
	}
	
	@Override
	public PhGoodsCategory findOne(Long id){
		return phGoodsCategoryRepository.findOne(id);
	}
}
