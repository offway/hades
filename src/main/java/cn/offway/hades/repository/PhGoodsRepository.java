package cn.offway.hades.repository;

import cn.offway.hades.domain.PhGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 商品表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsRepository extends JpaRepository<PhGoods, Long>, JpaSpecificationExecutor<PhGoods> {
    @Query(nativeQuery = true, value = "SELECT count(*) FROM ph_goods WHERE (`merchant_id` = ?1)")
    Optional<String> getCount(Long merchantId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `ph_goods` SET `merchant_logo` = ?2 , `merchant_name` = ?3 , `merchant_type` = ?4 WHERE (`merchant_id` = ?1)")
    void updateMerchantInfo(Long mid, String mLogo, String mName, String mType);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `ph_goods` SET `brand_logo` = ?2 , `brand_name` = ?3 WHERE (`brand_id` = ?1)")
    void updateBrandInfo(Long bid, String bLogo, String bName);

    @Query(nativeQuery = true, value = "SELECT * FROM `ph_goods` where `id` not in (SELECT `goods_id` FROM `ph_promotion_goods`) and `merchant_id` = ?1 and `brand_id` = ?2")
    Optional<List<PhGoods>> findAllRestGoods(Long mid, Long bid);

    @Query(nativeQuery = true, value = "SELECT * FROM `ph_goods` where `id` not in (SELECT `goods_id` FROM `ph_promotion_goods`) and `brand_id` = ?1")
    Optional<List<PhGoods>> findAllRestGoods(Long bid);
}
