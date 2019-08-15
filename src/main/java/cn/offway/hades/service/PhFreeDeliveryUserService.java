package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhFreeDeliveryUser;

/**
 * 免费送参与用户Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
public interface PhFreeDeliveryUserService{

    PhFreeDeliveryUser save(PhFreeDeliveryUser phFreeDeliveryUser);
	
    PhFreeDeliveryUser findOne(Long id);

    void delete(Long id);

    List<PhFreeDeliveryUser> save(List<PhFreeDeliveryUser> entities);
}
