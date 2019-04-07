package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhCapitalFlowService;

import cn.offway.hades.domain.PhCapitalFlow;
import cn.offway.hades.repository.PhCapitalFlowRepository;


/**
 * 资金流水Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhCapitalFlowServiceImpl implements PhCapitalFlowService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhCapitalFlowRepository phCapitalFlowRepository;
	
	@Override
	public PhCapitalFlow save(PhCapitalFlow phCapitalFlow){
		return phCapitalFlowRepository.save(phCapitalFlow);
	}
	
	@Override
	public PhCapitalFlow findOne(Long id){
		return phCapitalFlowRepository.findOne(id);
	}
}
