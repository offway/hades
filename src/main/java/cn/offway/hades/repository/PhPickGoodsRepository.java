package cn.offway.hades.repository;

import cn.offway.hades.domain.PhPickGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * OFFWAY优选商品明细Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhPickGoodsRepository extends JpaRepository<PhPickGoods, Long>, JpaSpecificationExecutor<PhPickGoods> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_pick_goods` WHERE (`pick_id` = ?1)")
    void deleteByPid(Long pid);

    @Query(nativeQuery = true, value = "SELECT `goods_id` FROM `ph_pick_goods` where (`pick_id` = ?1) and `goods_id` not in (SELECT `goods_id` FROM `ph_promotion_goods`)")
    Optional<List<Integer>> findAllRestByPid(Long gid);

    @Query(nativeQuery = true, value = "SELECT `id` FROM `ph_goods` where `id` not in (SELECT `goods_id` FROM `ph_pick_goods`)")
    Optional<List<Integer>> findAllRestGoods();
}
