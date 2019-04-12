package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.repository.PhBrandRepository;
import cn.offway.hades.service.PhBrandService;
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
    public Page<PhBrand> findAll(String name, Pageable pageable) {
        return phBrandRepository.findAll(new Specification<PhBrand>() {
            @Override
            public Predicate toPredicate(Root<PhBrand> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
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
