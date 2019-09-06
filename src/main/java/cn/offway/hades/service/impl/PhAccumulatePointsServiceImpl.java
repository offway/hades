package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhAccumulatePointsService;

import cn.offway.hades.domain.PhAccumulatePoints;
import cn.offway.hades.repository.PhAccumulatePointsRepository;


/**
 * 积分记录Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-06 15:21:47 Exp $
 */
@Service
public class PhAccumulatePointsServiceImpl implements PhAccumulatePointsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhAccumulatePointsRepository phAccumulatePointsRepository;
	
	@Override
	public PhAccumulatePoints save(PhAccumulatePoints phAccumulatePoints){
		return phAccumulatePointsRepository.save(phAccumulatePoints);
	}
	
	@Override
	public PhAccumulatePoints findOne(Long id){
		return phAccumulatePointsRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phAccumulatePointsRepository.delete(id);
	}

	@Override
	public List<PhAccumulatePoints> save(List<PhAccumulatePoints> entities){
		return phAccumulatePointsRepository.save(entities);
	}
}
