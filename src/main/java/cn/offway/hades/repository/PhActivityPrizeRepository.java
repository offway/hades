package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhActivityPrize;

/**
 * 活动奖品表-每日福利Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhActivityPrizeRepository extends JpaRepository<PhActivityPrize,Long>,JpaSpecificationExecutor<PhActivityPrize> {

	PhActivityPrize findByActivityIdAndUnionid(Long activityid,String unionid);
}
