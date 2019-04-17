package cn.offway.hades.repository;

import cn.offway.hades.domain.PhMerchantFare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商户运费模板Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhMerchantFareRepository extends JpaRepository<PhMerchantFare, Long>, JpaSpecificationExecutor<PhMerchantFare> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `ph_merchant_fare` SET `is_default` = '0' WHERE (`merchant_id` = ?1)")
    void makeAllToUnDefault(Long pid);
}
