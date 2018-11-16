package cn.offway.hades.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.offway.hades.service.PhLotteryTicketService;

@Component
public class ScheduledService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhLotteryTicketService phLotteryTicketService;
	
	/**
	 * 开奖通知
	 */
	@Scheduled(cron = "0 0 17 * * ?")
	public void scheduled() {
		logger.info("开奖通知定时任务开始");
		try {
			phLotteryTicketService.notice();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("开奖通知定时任务异常",e);
		}
		logger.info("开奖通知定时任务结束");
	}
}
