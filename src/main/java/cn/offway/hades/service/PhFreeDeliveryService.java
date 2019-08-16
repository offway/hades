package cn.offway.hades.service;


import cn.offway.hades.domain.PhFreeDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 免费送Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
public interface PhFreeDeliveryService {

    PhFreeDelivery save(PhFreeDelivery phFreeDelivery);

    PhFreeDelivery findOne(Long id);

    void delete(Long id);

    List<PhFreeDelivery> save(List<PhFreeDelivery> entities);

    Page<PhFreeDelivery> findAll(Pageable pageable);

    void deleteByproductIdInList(List<Long> id);
}
