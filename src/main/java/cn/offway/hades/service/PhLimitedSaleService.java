package cn.offway.hades.service;

import cn.offway.hades.domain.PhLimitedSale;

/**
 * 限量发售Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhLimitedSaleService{

	PhLimitedSale save(PhLimitedSale phLimitedSale);
	
	PhLimitedSale findOne(Long id);
}