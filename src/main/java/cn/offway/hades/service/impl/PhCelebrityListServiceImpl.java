package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhCelebrityListService;

import cn.offway.hades.domain.PhCelebrityList;
import cn.offway.hades.repository.PhCelebrityListRepository;


/**
 * 明星信息表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
@Service
public class PhCelebrityListServiceImpl implements PhCelebrityListService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhCelebrityListRepository phCelebrityListRepository;
	
	@Override
	public PhCelebrityList save(PhCelebrityList phCelebrityList){
		return phCelebrityListRepository.save(phCelebrityList);
	}
	
	@Override
	public PhCelebrityList findOne(Long id){
		return phCelebrityListRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phCelebrityListRepository.delete(id);
	}

	@Override
	public List<PhCelebrityList> save(List<PhCelebrityList> entities){
		return phCelebrityListRepository.save(entities);
	}
}
