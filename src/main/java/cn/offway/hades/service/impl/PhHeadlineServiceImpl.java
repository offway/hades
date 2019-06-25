package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhHeadlineService;

import cn.offway.hades.domain.PhHeadline;
import cn.offway.hades.repository.PhHeadlineRepository;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhHeadlineServiceImpl implements PhHeadlineService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhHeadlineRepository phHeadlineRepository;
	
	@Override
	public PhHeadline save(PhHeadline phHeadline){
		return phHeadlineRepository.save(phHeadline);
	}
	
	@Override
	public PhHeadline findOne(Long id){
		return phHeadlineRepository.findOne(id);
	}
}
