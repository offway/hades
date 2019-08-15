package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhFreeProduct;

/**
 * 免费送产品表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
public interface PhFreeProductService{

    PhFreeProduct save(PhFreeProduct phFreeProduct);
	
    PhFreeProduct findOne(Long id);

    void delete(Long id);

    List<PhFreeProduct> save(List<PhFreeProduct> entities);
}
