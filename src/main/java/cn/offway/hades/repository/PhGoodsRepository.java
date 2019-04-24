package cn.offway.hades.repository;

import cn.offway.hades.domain.PhGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

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
}
