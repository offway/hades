package cn.offway.hades.repository;

import cn.offway.hades.domain.PhSettlementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * 商户结算明细表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhSettlementDetailRepository extends JpaRepository<PhSettlementDetail, Long>, JpaSpecificationExecutor<PhSettlementDetail> {
    @Query(nativeQuery = true, value = "SELECT count(*) FROM ph_settlement_detail WHERE (`order_no` = ?1)")
    Optional<String> getCount(String orderNo);
}
