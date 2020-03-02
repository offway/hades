package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhFollowService;

import cn.offway.hades.domain.PhFollow;
import cn.offway.hades.repository.PhFollowRepository;


/**
 * 关注列表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
@Service
public class PhFollowServiceImpl implements PhFollowService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhFollowRepository phFollowRepository;
	
	@Override
	public PhFollow save(PhFollow phFollow){
		return phFollowRepository.save(phFollow);
	}
	
	@Override
	public PhFollow findOne(Long id){
		return phFollowRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phFollowRepository.delete(id);
	}

	@Override
	public List<PhFollow> save(List<PhFollow> entities){
		return phFollowRepository.save(entities);
	}
}
