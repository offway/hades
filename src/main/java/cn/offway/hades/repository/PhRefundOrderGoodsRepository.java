package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhRefundOrderGoods;

/**
 * 退换货后的订单商品Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-06 15:46:21 Exp $
 */
public interface PhRefundOrderGoodsRepository extends JpaRepository<PhRefundOrderGoods,Long>,JpaSpecificationExecutor<PhRefundOrderGoods> {

	/** 此处写一些自定义的方法 **/
}
