package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.hades.domain.PhWinningRecord;
import cn.offway.hades.repository.PhWinningRecordRepository;
import cn.offway.hades.service.PhWinningRecordService;


/**
 * 中奖用户信息Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhWinningRecordServiceImpl implements PhWinningRecordService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhWinningRecordRepository phWinningRecordRepository;
	
	@Override
	public PhWinningRecord save(PhWinningRecord phWinningRecord){
		return phWinningRecordRepository.save(phWinningRecord);
	}
	
	@Override
	public PhWinningRecord findOne(Long id){
		return phWinningRecordRepository.findOne(id);
	}
	
	@Override
	public int saveWin(Long productId,List<String> codes){
		return phWinningRecordRepository.saveWin(productId, codes);
	}
}
