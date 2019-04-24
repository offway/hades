package cn.offway.hades.repository;

import cn.offway.hades.domain.PhVoucherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 优惠券Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhVoucherInfoRepository extends JpaRepository<PhVoucherInfo, Long>, JpaSpecificationExecutor<PhVoucherInfo> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_voucher_info` WHERE (`voucher_project_id` = ?1)")
    void deleteByPid(Long pid);
}
