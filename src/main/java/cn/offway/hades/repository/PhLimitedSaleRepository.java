package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhLimitedSale;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 限量发售Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhLimitedSaleRepository extends JpaRepository<PhLimitedSale,Long>,JpaSpecificationExecutor<PhLimitedSale> {

	/** 此处写一些自定义的方法 **/

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE ph_limited_sale SET is_show = '0'")
    void issetshow0();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE ph_limited_sale SET is_show = '1' WHERE id = ?1")
    void issetshow1(Long id);
}
