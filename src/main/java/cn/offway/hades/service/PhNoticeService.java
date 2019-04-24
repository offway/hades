package cn.offway.hades.service;

import cn.offway.hades.domain.PhNotice;

/**
 * 消息通知Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhNoticeService{

	PhNotice save(PhNotice phNotice);
	
	PhNotice findOne(Long id);
}
