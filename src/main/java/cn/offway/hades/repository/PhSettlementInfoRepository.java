package cn.offway.hades.repository;

import cn.offway.hades.domain.PhSettlementInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * 商户结算表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhSettlementInfoRepository extends JpaRepository<PhSettlementInfo, Long>, JpaSpecificationExecutor<PhSettlementInfo> {
    @Query(nativeQuery = true, value = "SELECT count(*) FROM ph_settlement_info WHERE (`merchant_id` = ?1)")
    Optional<String> getCount(Long merchantId);
}
