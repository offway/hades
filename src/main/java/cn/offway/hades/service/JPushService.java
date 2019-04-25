package cn.offway.hades.service;

import java.util.Date;
import java.util.Map;

public interface JPushService {

	boolean updateScheduleTrigger(String businessId, String businessType, Date time);

	void deleteSchedule(String businessId, String businessType);

	boolean sendPush(String tilte, String alert, Map<String, String> extras);

	boolean createSingleSchedule(String businessId, String businessType, String name, Date time, String tilte,
			String alert, Map<String, String> extras);

	boolean sendPushUser(String tilte, String alert, Map<String, String> extras, String... alias);
	
}
