package cn.offway.hades.service;

import cn.offway.hades.domain.PhJpushSchedule;

/**
 * 极光推送定时任务Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhJpushScheduleService{

	PhJpushSchedule save(PhJpushSchedule phJpushSchedule);
	
	PhJpushSchedule findOne(Long id);

	PhJpushSchedule findByBusinessIdAndBusinessType(String businessId, String businessType);

	void delete(Long id);
}
