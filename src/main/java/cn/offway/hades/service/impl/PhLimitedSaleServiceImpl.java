package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhLimitedSaleService;

import cn.offway.hades.domain.PhLimitedSale;
import cn.offway.hades.repository.PhLimitedSaleRepository;


/**
 * 限量发售Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhLimitedSaleServiceImpl implements PhLimitedSaleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhLimitedSaleRepository phLimitedSaleRepository;
	
	@Override
	public PhLimitedSale save(PhLimitedSale phLimitedSale){
		return phLimitedSaleRepository.save(phLimitedSale);
	}
	
	@Override
	public PhLimitedSale findOne(Long id){
		return phLimitedSaleRepository.findOne(id);
	}
}
