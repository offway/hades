package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhGoods;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhArticleService;

import cn.offway.hades.domain.PhArticle;
import cn.offway.hades.repository.PhArticleRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * 文章Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhArticleServiceImpl implements PhArticleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhArticleRepository phArticleRepository;
	
	@Override
	public PhArticle save(PhArticle phArticle){
		return phArticleRepository.save(phArticle);
	}

	@Override
	public Page<PhArticle> findAll(Pageable pageable) {
		return phArticleRepository.findAll(pageable);
	}

	@Override
	public void del(Long id) {
		phArticleRepository.delete(id);
	}

	@Override
	public PhArticle findOne(Long id){
		return phArticleRepository.findOne(id);
	}

	@Override
	public Page<PhArticle> findAll(String name, String tag, String status, String title, String type, Pageable pageable) {
		return phArticleRepository.findAll(new Specification<PhArticle>() {
			@Override
			public Predicate toPredicate(Root<PhArticle> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				if (StringUtils.isNotBlank(name)){
					params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
				}
				if (StringUtils.isNotBlank(tag)){
					params.add(criteriaBuilder.like(root.get("tag"), "%" + tag + "%"));
				}
				if (StringUtils.isNotBlank(status)){
					params.add(criteriaBuilder.like(root.get("status"), "%" + status + "%"));
				}
				if (StringUtils.isNotBlank(title)){
					params.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
				}
				if (StringUtils.isNotBlank(type)){
					params.add(criteriaBuilder.like(root.get("type"), "%" + type + "%"));
				}
				Predicate[] predicates = new Predicate[params.size()];
				criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		}, pageable);
	}
}
