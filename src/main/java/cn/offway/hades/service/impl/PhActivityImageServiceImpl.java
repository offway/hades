package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhActivityImageService;

import cn.offway.hades.domain.PhActivityImage;
import cn.offway.hades.repository.PhActivityImageRepository;


/**
 * 活动图片表-每日福利Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhActivityImageServiceImpl implements PhActivityImageService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhActivityImageRepository phActivityImageRepository;
	
	@Override
	public PhActivityImage save(PhActivityImage phActivityImage){
		return phActivityImageRepository.save(phActivityImage);
	}
	
	@Override
	public List<PhActivityImage> save(List<PhActivityImage> phActivityImages){
		 return phActivityImageRepository.save(phActivityImages);
	}
	
	@Override
	public PhActivityImage findOne(Long id){
		return phActivityImageRepository.findOne(id);
	}
	
	@Override
	public List<PhActivityImage> findByActivityId(Long activityId){
		return phActivityImageRepository.findByActivityId(activityId);
	}
	
	@Override
	public void delete(List<PhActivityImage> phActivityImages){
		 phActivityImageRepository.delete(phActivityImages);
	}
	
	@Override
	public void delete(PhActivityImage phActivityImage){
		 phActivityImageRepository.delete(phActivityImage);
	}
}
