package cn.offway.hades.service;

import cn.offway.hades.domain.PhActivityBlacklist;

/**
 * 活动黑名单表-每日福利Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhActivityBlacklistService{

	PhActivityBlacklist save(PhActivityBlacklist phActivityBlacklist);
	
	PhActivityBlacklist findOne(Long id);
}
