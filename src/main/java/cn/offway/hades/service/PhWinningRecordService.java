package cn.offway.hades.service;

import cn.offway.hades.domain.PhWinningRecord;

/**
 * 中奖用户信息Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhWinningRecordService{

	PhWinningRecord save(PhWinningRecord phWinningRecord);
	
	PhWinningRecord findOne(Long id);
}
