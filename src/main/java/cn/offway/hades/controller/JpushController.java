package cn.offway.hades.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.DeviceType;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.DefaultResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.TagAliasResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.Notification.Builder;
import cn.jpush.api.schedule.ScheduleResult;
import cn.jpush.api.schedule.model.TriggerPayload;

public class JpushController {

	private Logger LOG = LoggerFactory.getLogger(this.getClass());

	public void push() {
		
		ClientConfig config = ClientConfig.getInstance();
		config.setApnsProduction(false);
		JPushClient jpushClient = new JPushClient("90f4a69575d4a51c877f5dad", "23f3d1476579eda19a0da872", null, config);
		
		
		
		PushPayload payload = buildPushObject_all_alias_alert("给你点赞了啦.","2a322c540b9642e98ce025e019bc1790");
		try {
			
			
			ScheduleResult scheduleResult = jpushClient.createSingleSchedule("定时推送", "2018-12-14 14:09:00", payload);
			LOG.info(scheduleResult.isResultOK()+"");
			
//			TriggerPayload trigger = TriggerPayload.newBuilder()
//	                .setSingleTime("2018-12-13 18:00:00")
//	                .buildSingle();
//			
//			jpushClient.updateScheduleTrigger(scheduleResult.getSchedule_id(), trigger);
//			jpushClient.deleteTag("LOTTERY_2", DeviceType.IOS.value());
//			Set<String> tagsToAdd = new HashSet<>();
//			tagsToAdd.add("LOTTERY_1");
//			DefaultResult defaultResult = jpushClient.updateDeviceTagAlias("161a3797c850fb49098", null, tagsToAdd, null);
//			LOG.info(""+defaultResult.isResultOK());
//	        TagAliasResult result = jpushClient.getDeviceTagAlias("161a3797c850fb49098");
//
//	        LOG.info(result.alias);
//	        LOG.info(result.tags.toString());
	    } catch (APIConnectionException e) {
	        LOG.error("Connection error. Should retry later. ", e);
	    } catch (APIRequestException e) {
	        LOG.error("Error response from JPush server. Should review and fix it. ", e);
	        LOG.info("HTTP Status: " + e.getStatus());
	        LOG.info("Error Code: " + e.getErrorCode());
	        LOG.info("Error Message: " + e.getErrorMessage());
	    }
		
		
		/*try {
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}*/
	}

	public static PushPayload buildPushObject_all_alias_alert(String content,String...registrationId) {
		Map<String, String> extras = new HashMap<>();
		extras.put("type", "测试");
		extras.put("seq", "002");
//		Notification.android(content, "title", extras);
		
		IosAlert alert = IosAlert.newBuilder().setTitleAndBody("自定义标题", null, content).build();
		
//		JsonObject a = new JsonObject();
//		a.addProperty("title", "自定义标题");
//		a.addProperty("subtitle", content);
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(registrationId))
				.setNotification(Notification.ios(alert, extras)
//						.setNotification(Notification.android(content, "title", extras)
	                    ).build();
	}
	
	public static void main(String[] args) {
		new JpushController().push();
	}
}
