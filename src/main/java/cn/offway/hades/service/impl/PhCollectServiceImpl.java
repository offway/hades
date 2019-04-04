package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhCollectService;

import cn.offway.hades.domain.PhCollect;
import cn.offway.hades.repository.PhCollectRepository;


/**
 * 收藏夹Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhCollectServiceImpl implements PhCollectService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhCollectRepository phCollectRepository;
	
	@Override
	public PhCollect save(PhCollect phCollect){
		return phCollectRepository.save(phCollect);
	}
	
	@Override
	public PhCollect findOne(Long id){
		return phCollectRepository.findOne(id);
	}
}
