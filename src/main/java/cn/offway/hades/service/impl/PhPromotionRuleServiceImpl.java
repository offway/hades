package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhPromotionRuleService;

import cn.offway.hades.domain.PhPromotionRule;
import cn.offway.hades.repository.PhPromotionRuleRepository;


/**
 * 促销活动规则Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPromotionRuleServiceImpl implements PhPromotionRuleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhPromotionRuleRepository phPromotionRuleRepository;
	
	@Override
	public PhPromotionRule save(PhPromotionRule phPromotionRule){
		return phPromotionRuleRepository.save(phPromotionRule);
	}
	
	@Override
	public PhPromotionRule findOne(Long id){
		return phPromotionRuleRepository.findOne(id);
	}
}
