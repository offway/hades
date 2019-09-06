package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhAccumulatePoints;

/**
 * 积分记录Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-06 15:21:47 Exp $
 */
public interface PhAccumulatePointsRepository extends JpaRepository<PhAccumulatePoints,Long>,JpaSpecificationExecutor<PhAccumulatePoints> {

	/** 此处写一些自定义的方法 **/
}
