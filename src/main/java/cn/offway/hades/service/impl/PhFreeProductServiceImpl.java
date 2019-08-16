package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhFreeProductService;

import cn.offway.hades.domain.PhFreeProduct;
import cn.offway.hades.repository.PhFreeProductRepository;


/**
 * 免费送产品表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
@Service
public class PhFreeProductServiceImpl implements PhFreeProductService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhFreeProductRepository phFreeProductRepository;
	
	@Override
	public PhFreeProduct save(PhFreeProduct phFreeProduct){
		return phFreeProductRepository.save(phFreeProduct);
	}
	
	@Override
	public PhFreeProduct findOne(Long id){
		return phFreeProductRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phFreeProductRepository.delete(id);
	}

	@Override
	public List<PhFreeProduct> save(List<PhFreeProduct> entities){
		return phFreeProductRepository.save(entities);
	}

	@Override
	public Page<PhFreeProduct> findAll(Pageable pageable){
		return phFreeProductRepository.findAll(pageable);
	}
}
