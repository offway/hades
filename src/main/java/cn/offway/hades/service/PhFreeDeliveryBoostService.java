package cn.offway.hades.service;


import cn.offway.hades.domain.PhFreeDeliveryBoost;

import java.util.List;

/**
 * 免费送助力Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
public interface PhFreeDeliveryBoostService {

    PhFreeDeliveryBoost save(PhFreeDeliveryBoost phFreeDeliveryBoost);

    PhFreeDeliveryBoost findOne(Long id);

    void delete(Long id);

    List<PhFreeDeliveryBoost> save(List<PhFreeDeliveryBoost> entities);

    Long getCountByPid(Long id);

    List<PhFreeDeliveryBoost> getListByPid(Long id);
}
