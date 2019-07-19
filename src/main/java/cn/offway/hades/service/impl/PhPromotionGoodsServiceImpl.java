package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhPromotionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhPromotionGoodsService;

import cn.offway.hades.domain.PhPromotionGoods;
import cn.offway.hades.repository.PhPromotionGoodsRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * 促销活动对应商品Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPromotionGoodsServiceImpl implements PhPromotionGoodsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhPromotionGoodsRepository phPromotionGoodsRepository;
	
	@Override
	public PhPromotionGoods save(PhPromotionGoods phPromotionGoods){
		return phPromotionGoodsRepository.save(phPromotionGoods);
	}
	
	@Override
	public PhPromotionGoods findOne(Long id){
		return phPromotionGoodsRepository.findOne(id);
	}

	@Override
	public void del(Long id) {
		phPromotionGoodsRepository.delBypromotionid(id);
	}

	@Override
	public List<PhPromotionGoods> findAllByPid(Long id) {
		return phPromotionGoodsRepository.findAll(new Specification<PhPromotionGoods>() {
			@Override
			public Predicate toPredicate(Root<PhPromotionGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				params.add(criteriaBuilder.equal(root.get("promotionId"), id));
				Predicate[] predicates = new Predicate[params.size()];
				criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		});
	}
}
