package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhShareRecord;
import cn.offway.hades.repository.PhShareRecordRepository;
import cn.offway.hades.service.PhShareRecordService;


/**
 * 分享记录表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhShareRecordServiceImpl implements PhShareRecordService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhShareRecordRepository phShareRecordRepository;
	
	@Override
	public PhShareRecord save(PhShareRecord phShareRecord){
		return phShareRecordRepository.save(phShareRecord);
	}
	
	@Override
	public PhShareRecord findOne(Long id){
		return phShareRecordRepository.findOne(id);
	}
}
