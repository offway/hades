package cn.offway.hades.repository;

import cn.offway.hades.domain.PhMerchantBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商户品牌关系Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhMerchantBrandRepository extends JpaRepository<PhMerchantBrand, Long>, JpaSpecificationExecutor<PhMerchantBrand> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_merchant_brand` WHERE (`merchant_id` = ?1)")
    void deleteByPid(Long pid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `ph_merchant_brand` SET `merchant_logo` = ?2 , `merchant_name` = ?3 WHERE (`merchant_id` = ?1)")
    void updateMerchantInfo(Long mid, String mLogo, String mName);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `ph_merchant_brand` SET `brand_logo` = ?2 , `brand_name` = ?3 WHERE (`brand_id` = ?1)")
    void updateBrandInfo(Long bid, String bLogo, String bName);
}
