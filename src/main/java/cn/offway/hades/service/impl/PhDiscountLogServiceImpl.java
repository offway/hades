package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhDiscountLogService;

import cn.offway.hades.domain.PhDiscountLog;
import cn.offway.hades.repository.PhDiscountLogRepository;


/**
 * 定时任务记录表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2019-12-25 14:31:38 Exp $
 */
@Service
public class PhDiscountLogServiceImpl implements PhDiscountLogService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhDiscountLogRepository phDiscountLogRepository;
	
	@Override
	public PhDiscountLog save(PhDiscountLog phDiscountLog){
		return phDiscountLogRepository.save(phDiscountLog);
	}
	
	@Override
	public PhDiscountLog findOne(Long id){
		return phDiscountLogRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phDiscountLogRepository.delete(id);
	}

	@Override
	public List<PhDiscountLog> save(List<PhDiscountLog> entities){
		return phDiscountLogRepository.save(entities);
	}
}
