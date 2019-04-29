package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhPreorderInfo;
import cn.offway.hades.repository.PhPreorderInfoRepository;
import cn.offway.hades.service.PhPreorderInfoService;



/**
 * 预生成订单Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPreorderInfoServiceImpl implements PhPreorderInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhPreorderInfoRepository phPreorderInfoRepository;
	
	@Override
	public PhPreorderInfo save(PhPreorderInfo phPreorderInfo){
		return phPreorderInfoRepository.save(phPreorderInfo);
	}
	
	@Override
	public PhPreorderInfo findOne(Long id){
		return phPreorderInfoRepository.findOne(id);
	}
	
	@Override
	public PhPreorderInfo findByOrderNoAndStatus(String orderno,String status){
		return phPreorderInfoRepository.findByOrderNoAndStatus(orderno, status);
	}
	
	@Override
	public int countByUserIdAndStatus(Long userId,String status){
		return phPreorderInfoRepository.countByUserIdAndStatus(userId, status);
	}
	
}
