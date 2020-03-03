package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhThemeGoodsService;

import cn.offway.hades.domain.PhThemeGoods;
import cn.offway.hades.repository.PhThemeGoodsRepository;


/**
 * 主题商品表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
@Service
public class PhThemeGoodsServiceImpl implements PhThemeGoodsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhThemeGoodsRepository phThemeGoodsRepository;
	
	@Override
	public PhThemeGoods save(PhThemeGoods phThemeGoods){
		return phThemeGoodsRepository.save(phThemeGoods);
	}
	
	@Override
	public PhThemeGoods findOne(Long id){
		return phThemeGoodsRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phThemeGoodsRepository.delete(id);
	}

	@Override
	public List<PhThemeGoods> save(List<PhThemeGoods> entities){
		return phThemeGoodsRepository.save(entities);
	}
}
