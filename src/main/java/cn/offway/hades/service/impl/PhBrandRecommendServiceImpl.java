package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhBrandRecommend;
import cn.offway.hades.repository.PhBrandRecommendRepository;
import cn.offway.hades.service.PhBrandRecommendService;
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
 * 品牌推荐表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
@Service
public class PhBrandRecommendServiceImpl implements PhBrandRecommendService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhBrandRecommendRepository phBrandRecommendRepository;

    @Override
    public PhBrandRecommend save(PhBrandRecommend phBrandRecommend) {
        return phBrandRecommendRepository.save(phBrandRecommend);
    }

    @Override
    public PhBrandRecommend findOne(Long id) {
        return phBrandRecommendRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phBrandRecommendRepository.delete(id);
    }

    @Override
    public Page<PhBrandRecommend> list(String name, String type, Pageable pageable) {
        return phBrandRecommendRepository.findAll(new Specification<PhBrandRecommend>() {
            @Override
            public Predicate toPredicate(Root<PhBrandRecommend> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(name)) {
                    params.add(cb.like(root.get("name"), "%" + name + "%"));
                }
                if (StringUtils.isNotBlank(type)) {
                    params.add(cb.equal(root.get("type"), type));
                }
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhBrandRecommend> save(List<PhBrandRecommend> entities) {
        return phBrandRecommendRepository.save(entities);
    }
}
