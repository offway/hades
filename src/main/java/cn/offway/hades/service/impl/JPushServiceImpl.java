package cn.offway.hades.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;
import cn.jpush.api.schedule.model.TriggerPayload;
import cn.offway.hades.domain.PhJpushSchedule;
import cn.offway.hades.properties.JPushProperties;
import cn.offway.hades.service.JPushService;
import cn.offway.hades.service.PhJpushScheduleService;

@Service
public class JPushServiceImpl implements JPushService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JPushClient jPushClient;
	
	@Autowired
	private JPushProperties jPushProperties;
	
	@Autowired
	private PhJpushScheduleService phJpushScheduleService;
	
	/**
	 * 极光推送-带参数
	 * @param tilte
	 * @param alert
	 * @param type 0-H5,1-精选文章,2-活动
	 * @param content 
	 * @return
	 */
	@Override
	public boolean sendPush(String tilte, String alert, Map<String, String> extras){
		try {
			PushResult pushResult = jPushClient.sendPush(buildPushAll(tilte, alert, extras));
			String resultJson = JSON.toJSONString(pushResult);
			logger.info("极光推送响应:{}",resultJson);
			boolean result = pushResult.isResultOK();
			if(!result){
				logger.error("极光推送失败,返回:{}",resultJson);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("极光推送异常",e);
			return false;
		}
	}
	/**
	 * 创建定时推送-带参数
	 * @param name
	 * @param time
	 * @param tilte
	 * @param alert
	 * @param type 0-H5,1-精选文章,2-活动
	 * @return
	 */
	@Override
	public boolean createSingleSchedule(String businessId, String businessType, String name, Date time, String tilte, String alert, Map<String, String> extras) {

		try {
			
			PhJpushSchedule schedule = phJpushScheduleService.findByBusinessIdAndBusinessType(businessId, businessType);
			if(null != schedule){
				return false;
			}
			
			ScheduleResult scheduleResult = jPushClient.createSingleSchedule(name, DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss"),
					buildPushAll(tilte, alert, extras));
			String resultJson = JSON.toJSONString(scheduleResult);
			logger.info("创建极光定时推送相应:{}",resultJson);
			boolean result = scheduleResult.isResultOK();
			if(result){
				PhJpushSchedule phJpushSchedule = new PhJpushSchedule();
				phJpushSchedule.setBusinessId(businessId);
				phJpushSchedule.setBusinessType(businessType);
				phJpushSchedule.setCreateTime(new Date());
				phJpushSchedule.setName(name);
				phJpushSchedule.setScheduleId(scheduleResult.getSchedule_id());
				phJpushSchedule.setTime(time);
				phJpushScheduleService.save(phJpushSchedule);
			}else{
				logger.error("创建极光定时推送失败,返回:{}",resultJson);
			}
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建极光定时推送异常",e);
			return false;
		}
	}
	
	/**
	 * 更新定时推送
	 * @param scheduleId
	 * @param time
	 * @return
	 */
	@Override
	public boolean updateScheduleTrigger(String businessId, String businessType, Date time) {
		
		try {
			
			PhJpushSchedule phJpushSchedule = phJpushScheduleService.findByBusinessIdAndBusinessType(businessId, businessType);
			if(null == phJpushSchedule){
				return false;
			}
			
			TriggerPayload trigger = TriggerPayload.newBuilder()
					.setSingleTime(DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss")).buildSingle();
			
			ScheduleResult scheduleResult = jPushClient.updateScheduleTrigger(phJpushSchedule.getScheduleId(), trigger);
			String resultJson = JSON.toJSONString(scheduleResult);
			logger.info("修改极光定时推送相应:{}", resultJson);
			boolean result = scheduleResult.isResultOK();
			if (!result) {
				logger.error("修改极光定时推送失败,返回:{}", resultJson);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改极光定时推送异常", e);
			return false;
		}
	}
	
	/**
	 * 删除定时推送
	 * @param scheduleId
	 * @param time
	 * @return
	 */
	@Override
	public void deleteSchedule(String businessId, String businessType) {
		
		try {
			
			PhJpushSchedule phJpushSchedule = phJpushScheduleService.findByBusinessIdAndBusinessType(businessId, businessType);
			if(null != phJpushSchedule){
				jPushClient.deleteSchedule(phJpushSchedule.getScheduleId());
				phJpushScheduleService.delete(phJpushSchedule.getId());
				logger.info("删除定时推送成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除定时推送异常", e);
		}
	}
	

	/**
	 * 带参数推送所有用户
	 * 
	 * @param content
	 * @param extras
	 * @return
	 */
	public  PushPayload buildPushAll(String tilte, String alert, Map<String, String> extras) {

		Notification notification = Notification.newBuilder()
				.addPlatformNotification(
						AndroidNotification.newBuilder().setAlert(alert).setTitle(tilte).addExtras(extras).build())
				.addPlatformNotification(IosNotification.newBuilder()
						.setAlert(IosAlert.newBuilder().setTitleAndBody(tilte, null, alert).build()).addExtras(extras)
						.build())
				.build();

		Audience audience = Audience.alias("2a322c540b9642e98ce025e019bc1790","9016fa43d9f443e8bab1b00085d545ba","ff8b026042724ec792ff8a6bffa0ea9e");
		
		if(jPushProperties.getApnsProduction()){
			audience = Audience.all();
		}
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(audience)
				.setNotification(notification).build();
	}

}
