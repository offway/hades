package cn.offway.hades.repository;

import cn.offway.hades.domain.PhGoodsStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品库存Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsStockRepository extends JpaRepository<PhGoodsStock, Long>, JpaSpecificationExecutor<PhGoodsStock> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_goods_stock` WHERE (`goods_id` = ?1)")
    void deleteByPid(Long pid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `ph_goods_stock` SET `merchant_logo` = ?2 , `merchant_name` = ?3 WHERE (`merchant_id` = ?1)")
    void updateMerchantInfo(Long mid, String mLogo, String mName);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `ph_goods_stock` SET `brand_logo` = ?2 , `brand_name` = ?3 WHERE (`brand_id` = ?1)")
    void updateBrandInfo(Long bid, String bLogo, String bName);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `ph_goods_stock` SET `price` = ?2 WHERE (`goods_id` = ?1)")
    void updateByPid(Long pid, Double value);
}
