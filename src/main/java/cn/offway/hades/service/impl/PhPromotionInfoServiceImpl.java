package cn.offway.hades.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhPromotionInfoService;

import cn.offway.hades.domain.PhPromotionInfo;
import cn.offway.hades.repository.PhPromotionInfoRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * 促销活动Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPromotionInfoServiceImpl implements PhPromotionInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhPromotionInfoRepository phPromotionInfoRepository;
	
	@Override
	public PhPromotionInfo save(PhPromotionInfo phPromotionInfo){
		return phPromotionInfoRepository.save(phPromotionInfo);
	}
	
	@Override
	public PhPromotionInfo findOne(Long id){
		return phPromotionInfoRepository.findOne(id);
	}

	@Override
	public Page<PhPromotionInfo> findAll(String type,String status,String mode,Pageable pageable) {
		return phPromotionInfoRepository.findAll(new Specification<PhPromotionInfo>() {
			@Override
			public Predicate toPredicate(Root<PhPromotionInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> promotionInfos = new ArrayList<>();
				if (StringUtils.isNotBlank(type)){
					promotionInfos.add(criteriaBuilder.like(root.get("type"), "%" + type + "%"));
				}
				if (StringUtils.isNotBlank(status)){
					promotionInfos.add(criteriaBuilder.like(root.get("status"), "%" + status + "%"));
				}
				if (StringUtils.isNotBlank(mode)){
					promotionInfos.add(criteriaBuilder.like(root.get("mode"), "%" + mode + "%"));
				}
				Predicate[] predicates = new Predicate[promotionInfos.size()];
				criteriaQuery.where(promotionInfos.toArray(predicates));
				return null;
			}
		},pageable);
	}

	@Override
	public void del(Long id) {
		phPromotionInfoRepository.delete(id);
	}
}
