package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhGoodsImageService;

import cn.offway.hades.domain.PhGoodsImage;
import cn.offway.hades.repository.PhGoodsImageRepository;


/**
 * 商品图片Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsImageServiceImpl implements PhGoodsImageService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsImageRepository phGoodsImageRepository;
	
	@Override
	public PhGoodsImage save(PhGoodsImage phGoodsImage){
		return phGoodsImageRepository.save(phGoodsImage);
	}
	
	@Override
	public PhGoodsImage findOne(Long id){
		return phGoodsImageRepository.findOne(id);
	}
}
