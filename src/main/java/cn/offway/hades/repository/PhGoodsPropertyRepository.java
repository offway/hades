package cn.offway.hades.repository;

import cn.offway.hades.domain.PhGoodsProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品属性Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsPropertyRepository extends JpaRepository<PhGoodsProperty, Long>, JpaSpecificationExecutor<PhGoodsProperty> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_goods_property` WHERE (`goods_id` = ?1)")
    void deleteByPid(Long pid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_goods_property` WHERE (`goods_stock_id` = ?1)")
    void deleteByStockId(Long sid);
}
