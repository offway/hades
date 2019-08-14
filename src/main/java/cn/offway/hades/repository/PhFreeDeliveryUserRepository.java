package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhFreeDeliveryUser;

/**
 * 免费送参与用户Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
public interface PhFreeDeliveryUserRepository extends JpaRepository<PhFreeDeliveryUser,Long>,JpaSpecificationExecutor<PhFreeDeliveryUser> {

	/** 此处写一些自定义的方法 **/
}
