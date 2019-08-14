package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhFreeDelivery;

/**
 * 免费送Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
public interface PhFreeDeliveryService{

    PhFreeDelivery save(PhFreeDelivery phFreeDelivery);
	
    PhFreeDelivery findOne(Long id);

    void delete(Long id);

    List<PhFreeDelivery> save(List<PhFreeDelivery> entities);
}
