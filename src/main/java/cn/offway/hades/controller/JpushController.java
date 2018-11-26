package cn.offway.hades.controller;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import cn.jpush.api.push.model.notification.Notification;

public class JpushController {

	private Logger LOG = LoggerFactory.getLogger(this.getClass());

	public void push() {
		
		ClientConfig config = ClientConfig.getInstance();
		config.setApnsProduction(true);
		JPushClient jpushClient = new JPushClient("90f4a69575d4a51c877f5dad", "23f3d1476579eda19a0da872", null, config);
		
		
		
//		PushPayload payload = buildPushObject_all_alias_alert("给你又点赞了.","161a3797c850fb49098");

		
		try {
			
			jpushClient.deleteTag("LOTTERY_2", DeviceType.IOS.value());
//			Set<String> tagsToAdd = new HashSet<>();
//			tagsToAdd.add("LOTTERY_1");
//			DefaultResult defaultResult = jpushClient.updateDeviceTagAlias("161a3797c850fb49098", null, tagsToAdd, null);
//			LOG.info(""+defaultResult.isResultOK());
	        TagAliasResult result = jpushClient.getDeviceTagAlias("161a3797c850fb49098");

	        LOG.info(result.alias);
	        LOG.info(result.tags.toString());
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
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(registrationId))
				.setNotification(Notification.alert(content)).build();
	}
	
	public static void main(String[] args) {
		new JpushController().push();
	}
}