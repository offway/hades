package cn.offway.hades.repository;

import cn.offway.hades.domain.PhBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Banner管理Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhBannerRepository extends JpaRepository<PhBanner, Long>, JpaSpecificationExecutor<PhBanner> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update `ph_banner` set `sort` = `sort` + 1 where `sort` >= ?2 AND `status` = 1 AND `position` = ?1")
    void resort(String position, Long sort);

    @Query(nativeQuery = true, value = "SELECT max(sort) as a FROM ph_banner where status = 1 AND position = ?1")
    Optional<String> getMaxSort(String position);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update `ph_banner` set `status` = 0 where `status` = 1 and `end_time` <= NOW()")
    void updatetime();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update `ph_banner` set `status` = 0 where `position` = 4 ")
    void updateSamestate();
}
