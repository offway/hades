package cn.offway.hades.service;


import cn.offway.hades.domain.PhFreeProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 免费送产品表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
public interface PhFreeProductService {

    PhFreeProduct save(PhFreeProduct phFreeProduct);

    PhFreeProduct findOne(Long id);

    void delete(Long id);

    List<PhFreeProduct> save(List<PhFreeProduct> entities);

    Page<PhFreeProduct> findAll(Pageable pageable);

    void deleteList(List<Long> id);
}
