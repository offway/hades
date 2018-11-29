package cn.offway.hades.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.offway.hades.service.PhActivityPrizeService;
import cn.offway.hades.service.PhLotteryTicketService;

@Component
public class ScheduledService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhLotteryTicketService phLotteryTicketService;
	
	@Autowired
	private PhActivityPrizeService phActivityPrizeService;
	
	/**
	 * 开奖通知-限时豪礼
	 */
//	@Scheduled(cron = "0 0 17 * * ?")
	public void scheduled() {
		logger.info("开奖通知-限时豪礼定时任务开始");
		try {
			phLotteryTicketService.notice();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("开奖通知-限时豪礼定时任务异常",e);
		}
		logger.info("开奖通知-限时豪礼定时任务结束");
	}
	
	/**
	 * 开奖通知-每日福利
	 */
//	@Scheduled(cron = "0 0 11 * * ?")
	public void dailyOpen() {
		logger.info("开奖通知-每日福利定时任务开始");
		try {
			phActivityPrizeService.open();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("开奖通知-每日福利定时任务异常",e);
		}
		logger.info("开奖通知-每日福利定时任务结束");
	}
}
