package cn.offway.hades.service;

import java.util.Date;
import java.util.Map;

public interface JPushService {

	boolean createSingleSchedule(String businessId, String businessType, String name, Date time, String tilte,
			String alert, String type, String content);

	boolean updateScheduleTrigger(String businessId, String businessType, Date time);

	void deleteSchedule(String businessId, String businessType);

	boolean sendPush(String tilte, String alert, String type, String content);

}
