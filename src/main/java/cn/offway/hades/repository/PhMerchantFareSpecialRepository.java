package cn.offway.hades.repository;

import cn.offway.hades.domain.PhMerchantFareSpecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商户运费特殊表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhMerchantFareSpecialRepository extends JpaRepository<PhMerchantFareSpecial, Long>, JpaSpecificationExecutor<PhMerchantFareSpecial> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_merchant_fare_special` WHERE (`merchant_fare_id` = ?1)")
    void deleteByPid(Long pid);
}
