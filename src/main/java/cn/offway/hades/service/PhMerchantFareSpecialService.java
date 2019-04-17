package cn.offway.hades.service;

import cn.offway.hades.domain.PhMerchantFareSpecial;

/**
 * 商户运费特殊表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhMerchantFareSpecialService{

	PhMerchantFareSpecial save(PhMerchantFareSpecial phMerchantFareSpecial);
	
	PhMerchantFareSpecial findOne(Long id);
}
