package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhFreeDeliveryBoost;

/**
 * 免费送助力Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
public interface PhFreeDeliveryBoostService{

    PhFreeDeliveryBoost save(PhFreeDeliveryBoost phFreeDeliveryBoost);
	
    PhFreeDeliveryBoost findOne(Long id);

    void delete(Long id);

    List<PhFreeDeliveryBoost> save(List<PhFreeDeliveryBoost> entities);
}
