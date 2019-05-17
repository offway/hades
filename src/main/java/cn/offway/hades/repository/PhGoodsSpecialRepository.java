package cn.offway.hades.repository;

import cn.offway.hades.domain.PhGoodsSpecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 特殊商品Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsSpecialRepository extends JpaRepository<PhGoodsSpecial, Long>, JpaSpecificationExecutor<PhGoodsSpecial> {
    @Query(nativeQuery = true, value = "SELECT count(*) FROM ph_goods_special WHERE (`goods_id` = ?1)")
    Optional<String> getCount(Long gid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_goods_special` WHERE (`goods_id` = ?1)")
    void deleteByGid(Long gid);
}
