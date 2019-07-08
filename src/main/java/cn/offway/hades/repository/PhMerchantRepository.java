package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhMerchant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商户表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhMerchantRepository extends JpaRepository<PhMerchant,Long>,JpaSpecificationExecutor<PhMerchant> {

	/** 此处写一些自定义的方法 **/
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update `ph_merchant` set `sort` = `sort` + 1 where `type` = 1 and `sort` >= ?1")
    void resort(Long sort);
}
