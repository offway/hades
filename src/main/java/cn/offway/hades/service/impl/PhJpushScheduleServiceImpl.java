package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhJpushSchedule;
import cn.offway.hades.repository.PhJpushScheduleRepository;
import cn.offway.hades.service.PhJpushScheduleService;



/**
 * 极光推送定时任务Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhJpushScheduleServiceImpl implements PhJpushScheduleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhJpushScheduleRepository phJpushScheduleRepository;
	
	@Override
	public PhJpushSchedule save(PhJpushSchedule phJpushSchedule){
		return phJpushScheduleRepository.save(phJpushSchedule);
	}
	
	@Override
	public PhJpushSchedule findOne(Long id){
		return phJpushScheduleRepository.findOne(id);
	}
	
	@Override
	public PhJpushSchedule findByBusinessIdAndBusinessType(String businessId,String businessType){
		return phJpushScheduleRepository.findByBusinessIdAndBusinessType(businessId, businessType);
	}
	
	@Override
	public void delete(Long id){
		phJpushScheduleRepository.delete(id);
	}
}
