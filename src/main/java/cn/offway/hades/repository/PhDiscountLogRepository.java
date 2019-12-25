package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhDiscountLog;

/**
 * 定时任务记录表Repository接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2019-12-25 14:31:38 Exp $
 */
public interface PhDiscountLogRepository extends JpaRepository<PhDiscountLog,Long>,JpaSpecificationExecutor<PhDiscountLog> {

	/** 此处写一些自定义的方法 **/
}
