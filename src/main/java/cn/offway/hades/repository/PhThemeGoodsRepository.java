package cn.offway.hades.repository;

import cn.offway.hades.domain.PhThemeGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 主题商品表Repository接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
public interface PhThemeGoodsRepository extends JpaRepository<PhThemeGoods, Long>, JpaSpecificationExecutor<PhThemeGoods> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_theme_goods` WHERE (`theme_id` = ?1)")
    void deleteByPid(Long pid);
}
