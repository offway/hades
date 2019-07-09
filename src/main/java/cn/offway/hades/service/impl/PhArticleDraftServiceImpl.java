package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhArticleDraft;
import cn.offway.hades.repository.PhArticleDraftRepository;
import cn.offway.hades.service.PhArticleDraftService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
public class PhArticleDraftServiceImpl implements PhArticleDraftService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhArticleDraftRepository phArticleRepository;

    @Override
    public PhArticleDraft save(PhArticleDraft phArticle) {
        return phArticleRepository.save(phArticle);
    }

    @Override
    public Page<PhArticleDraft> findAll(Pageable pageable) {
        return phArticleRepository.findAll(pageable);
    }

    @Override
    public void del(Long id) {
        phArticleRepository.delete(id);
    }

    @Override
    public PhArticleDraft findOne(Long id) {
        return phArticleRepository.findOne(id);
    }

    @Override
    public Page<PhArticleDraft> findAll(String name, String title, Pageable pageable) {
        return phArticleRepository.findAll(new Specification<PhArticleDraft>() {
            @Override
            public Predicate toPredicate(Root<PhArticleDraft> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(name)) {
                    params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }
                if (StringUtils.isNotBlank(title)) {
                    params.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }
}
