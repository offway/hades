package cn.offway.hades.service;

import cn.offway.hades.domain.PhConfig;

/**
 * 配置Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhConfigService{

	PhConfig save(PhConfig phConfig);
	
	PhConfig findOne(Long id);

	String findContentByName(String name);
}
