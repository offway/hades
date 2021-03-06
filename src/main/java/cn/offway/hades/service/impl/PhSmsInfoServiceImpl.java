package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhSmsInfoService;

import cn.offway.hades.domain.PhSmsInfo;
import cn.offway.hades.repository.PhSmsInfoRepository;


/**
 * 短信记录Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhSmsInfoServiceImpl implements PhSmsInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhSmsInfoRepository phSmsInfoRepository;
	
	@Override
	public PhSmsInfo save(PhSmsInfo phSmsInfo){
		return phSmsInfoRepository.save(phSmsInfo);
	}
	
	@Override
	public PhSmsInfo findOne(Long id){
		return phSmsInfoRepository.findOne(id);
	}
}
