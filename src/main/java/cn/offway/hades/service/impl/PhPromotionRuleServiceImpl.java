package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhPromotionGoods;
import cn.offway.hades.domain.PhPromotionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhPromotionRuleService;

import cn.offway.hades.domain.PhPromotionRule;
import cn.offway.hades.repository.PhPromotionRuleRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;


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

	@Override
	public Page<PhPromotionRule> findAll(org.springframework.data.domain.Pageable pageable) {
		return phPromotionRuleRepository.findAll(new Specification<PhPromotionRule>() {
			@Override
			public Predicate toPredicate(Root<PhPromotionRule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				return null;
			}
		},pageable);
	}

	@Override
	public void del(Long id) {
		phPromotionRuleRepository.delBypromotionid(id);
	}

	@Override
	public List<PhPromotionRule> findAllByPid(Long id) {
		return phPromotionRuleRepository.findAll(new Specification<PhPromotionRule>() {
			@Override
			public Predicate toPredicate(Root<PhPromotionRule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				params.add(criteriaBuilder.equal(root.get("promotionId"), id));
				Predicate[] predicates = new Predicate[params.size()];
				criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		});
	}
}
