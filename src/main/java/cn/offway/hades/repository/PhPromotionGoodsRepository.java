package cn.offway.hades.repository;

import cn.offway.hades.domain.PhActivityImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhPromotionGoods;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 促销活动对应商品Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhPromotionGoodsRepository extends JpaRepository<PhPromotionGoods,Long>,JpaSpecificationExecutor<PhPromotionGoods> {

    @Modifying
    @Transactional
	/** 此处写一些自定义的方法 **/
    @Query(nativeQuery=true,value="DELETE FROM `ph_promotion_goods` WHERE `promotion_id` = ?1")
    int delBypromotionid(Long id);
}
