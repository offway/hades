package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhActivityBlacklistService;

import cn.offway.hades.domain.PhActivityBlacklist;
import cn.offway.hades.repository.PhActivityBlacklistRepository;


/**
 * 活动黑名单表-每日福利Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhActivityBlacklistServiceImpl implements PhActivityBlacklistService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhActivityBlacklistRepository phActivityBlacklistRepository;
	
	@Override
	public PhActivityBlacklist save(PhActivityBlacklist phActivityBlacklist){
		return phActivityBlacklistRepository.save(phActivityBlacklist);
	}
	
	@Override
	public PhActivityBlacklist findOne(Long id){
		return phActivityBlacklistRepository.findOne(id);
	}
	
	@Override
	public int countByUnionid(String unionid){
		return phActivityBlacklistRepository.countByUnionid(unionid);
	}
}
