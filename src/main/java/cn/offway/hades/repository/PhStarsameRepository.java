package cn.offway.hades.repository;

import cn.offway.hades.domain.PhStarsame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 明星同款Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhStarsameRepository extends JpaRepository<PhStarsame, Long>, JpaSpecificationExecutor<PhStarsame> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update `ph_starsame` set `sort` = `sort` + 1 where `sort` >= ?1")
    void resort(Long sort);

    @Transactional
    @Modifying
//    @Query(nativeQuery = true, value = "UPDATE `ph_starsame` SET `sort` = NULL WHERE `sort` >= 6")
    @Query(nativeQuery = true, value = "UPDATE `ph_starsame` SET `sort` = 999 WHERE `sort` >= 6 or `sort` is null")
    void resetSort();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE ph_starsame SET sort_mini = sort")
    void updateSameSort();
}
