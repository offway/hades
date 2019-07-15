package cn.offway.hades.service;

import cn.offway.hades.domain.PhPromotionRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 促销活动规则Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhPromotionRuleService{

	PhPromotionRule save(PhPromotionRule phPromotionRule);
	
	PhPromotionRule findOne(Long id);

	Page<PhPromotionRule> findAll(Pageable pageable);
}
