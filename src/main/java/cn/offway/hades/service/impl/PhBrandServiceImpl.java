package cn.offway.hades.service.impl;

import cn.offway.hades.config.AsciiPredicate;
import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.repository.PhBrandRepository;
import cn.offway.hades.service.PhBrandService;
import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * 品牌库Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhBrandServiceImpl implements PhBrandService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhBrandRepository phBrandRepository;

    @Override
    public PhBrand save(PhBrand phBrand) {
        return phBrandRepository.save(phBrand);
    }

    @Override
    public Page<PhBrand> findAll(Pageable pageable) {
        return phBrandRepository.findAll(pageable);
    }

    @Override
    public void del(Long id) {
        phBrandRepository.delete(id);
    }

    @Override
    public void resort(Long sort) {
        phBrandRepository.resort(sort);
    }

    @Override
    public Page<PhBrand> findAll(String name, String type, String status, Pageable pageable) {
        return phBrandRepository.findAll(new Specification<PhBrand>() {
            @Override
            public Predicate toPredicate(Root<PhBrand> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                if (!"".equals(type)) {
                    params.add(criteriaBuilder.equal(root.get("type"), type));
                }
                if (!"".equals(status)) {
                    params.add(criteriaBuilder.equal(root.get("status"), status));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhBrand> findAll(String prefix) {
        return phBrandRepository.findAll(new Specification<PhBrand>() {
            @Override
            public Predicate toPredicate(Root<PhBrand> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if ("#".equals(prefix)) {
                    params.add(ascii((CriteriaBuilderImpl) criteriaBuilder, root.get("name")));
                } else {
                    params.add(criteriaBuilder.like(root.get("name"), prefix + "%"));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    public <Y extends Comparable<? super Y>> Predicate ascii(CriteriaBuilderImpl criteriaBuilder,
                                                             Expression<? extends Y> expression) {
        return new AsciiPredicate<>(criteriaBuilder, expression, null);
    }

    @Override
    public List<PhBrand> findAll(Long pid) {
        return phBrandRepository.findAll(new Specification<PhBrand>() {
            @Override
            public Predicate toPredicate(Root<PhBrand> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //TODO merchant id
                return null;
            }
        });
    }

    @Override
    public Long getMaxSort() {
        Optional<String> res = phBrandRepository.getMaxSort();
        if (res.isPresent()) {
            return Long.valueOf(String.valueOf(res.get()));
        } else {
            return 0L;
        }
    }

    @Override
    public List<PhBrand> findAll() {
        return phBrandRepository.findAll();
    }

    @Override
    public PhBrand findOne(Long id) {
        return phBrandRepository.findOne(id);
    }
}
