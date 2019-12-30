package cn.offway.hades.repository;

import cn.offway.hades.domain.PhMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商户表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhMerchantRepository extends JpaRepository<PhMerchant, Long>, JpaSpecificationExecutor<PhMerchant> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update `ph_merchant` set `sort` = `sort` + 1 where `type` = 1 and `sort` >= ?1")
    void resort(Long sort);

    @Query(nativeQuery = true, value = "SELECT a.id,a.name,a.logo,count(b.id) as ct,group_concat(b.id) as ids FROM ph_merchant a left join ph_merchant_brand b on a.id = b.merchant_id group by a.id")
    List<Object[]> stat();

    @Query(nativeQuery = true, value = "SELECT a.order_no,count(a.id) as ct,sum(b.goods_count) as tt,group_concat(b.id) as ids FROM ph_order_info a left join ph_order_goods b on a.order_no = b.order_no where a.merchant_id = ?1 and a.status in (1,2,3) and a.order_no not in (SELECT order_no FROM ph_refund where status = 4) group by a.order_no")
    List<Object[]> statOrder(Long id);
}
