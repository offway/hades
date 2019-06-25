package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhHeadlineTypeService;

import cn.offway.hades.domain.PhHeadlineType;
import cn.offway.hades.repository.PhHeadlineTypeRepository;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhHeadlineTypeServiceImpl implements PhHeadlineTypeService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhHeadlineTypeRepository phHeadlineTypeRepository;
	
	@Override
	public PhHeadlineType save(PhHeadlineType phHeadlineType){
		return phHeadlineTypeRepository.save(phHeadlineType);
	}
	
	@Override
	public PhHeadlineType findOne(Long id){
		return phHeadlineTypeRepository.findOne(id);
	}
}
