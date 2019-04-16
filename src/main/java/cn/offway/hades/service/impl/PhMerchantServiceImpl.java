package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.repository.PhMerchantRepository;
import cn.offway.hades.service.PhMerchantService;
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
 * 商户表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhMerchantServiceImpl implements PhMerchantService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhMerchantRepository phMerchantRepository;

    @Override
    public PhMerchant save(PhMerchant phMerchant) {
        return phMerchantRepository.save(phMerchant);
    }

    @Override
    public Page<PhMerchant> findAll(Pageable pageable) {
        return phMerchantRepository.findAll(pageable);
    }

    @Override
    public Page<PhMerchant> findAll(String name, Pageable pageable) {
        return phMerchantRepository.findAll(new Specification<PhMerchant>() {
            @Override
            public Predicate toPredicate(Root<PhMerchant> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhMerchant> findAll() {
        return phMerchantRepository.findAll();
    }

    @Override
    public List<PhMerchant> findAll(Long id) {
        return phMerchantRepository.findAll(new Specification<PhMerchant>() {
            @Override
            public Predicate toPredicate(Root<PhMerchant> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("id"), id));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public void del(Long id) {
        phMerchantRepository.delete(id);
    }

    @Override
    public PhMerchant findOne(Long id) {
        return phMerchantRepository.findOne(id);
    }
}
