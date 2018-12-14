package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhJpushSchedule;


/**
 * 极光推送定时任务Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhJpushScheduleRepository extends JpaRepository<PhJpushSchedule,Long>,JpaSpecificationExecutor<PhJpushSchedule> {

	PhJpushSchedule findByBusinessIdAndBusinessType(String businessId,String businessType);
}
