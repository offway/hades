package cn.offway.hades.service;

import cn.offway.hades.domain.PhCollect;

/**
 * 收藏夹Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhCollectService{

	PhCollect save(PhCollect phCollect);
	
	PhCollect findOne(Long id);
}
