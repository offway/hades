package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhInviteRecord;
import cn.offway.hades.repository.PhInviteRecordRepository;
import cn.offway.hades.service.PhInviteRecordService;


/**
 * 邀请记录表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhInviteRecordServiceImpl implements PhInviteRecordService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhInviteRecordRepository phInviteRecordRepository;
	
	@Override
	public PhInviteRecord save(PhInviteRecord phInviteRecord){
		return phInviteRecordRepository.save(phInviteRecord);
	}
	
	@Override
	public PhInviteRecord findOne(Long id){
		return phInviteRecordRepository.findOne(id);
	}
}
