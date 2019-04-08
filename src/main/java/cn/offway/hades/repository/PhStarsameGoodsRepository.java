package cn.offway.hades.repository;

import cn.offway.hades.domain.PhStarsameGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 明星同款商品Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhStarsameGoodsRepository extends JpaRepository<PhStarsameGoods, Long>, JpaSpecificationExecutor<PhStarsameGoods> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_starsame_goods` WHERE (`starsame_id` = ?1)")
    void deleteByPid(Long pid);
}
