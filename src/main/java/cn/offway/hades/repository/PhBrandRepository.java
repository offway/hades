package cn.offway.hades.repository;

import cn.offway.hades.domain.PhBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 品牌库Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhBrandRepository extends JpaRepository<PhBrand, Long>, JpaSpecificationExecutor<PhBrand> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update `ph_brand` set `sort` = `sort` + 1 where `sort` >= ?1")
    void resort(Long sort);
}
