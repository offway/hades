package cn.offway.hades.service;

import cn.offway.hades.domain.PhInviteRecord;

/**
 * 邀请记录表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhInviteRecordService{

	PhInviteRecord save(PhInviteRecord phInviteRecord);
	
	PhInviteRecord findOne(Long id);
}
