package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.repository.PhMerchantRepository;
import cn.offway.hades.service.PhMerchantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<PhMerchant> findAll(String name, String type, Pageable pageable) {
        return phMerchantRepository.findAll(new Specification<PhMerchant>() {
            @Override
            public Predicate toPredicate(Root<PhMerchant> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (!"".equals(type)) {
                    params.add(criteriaBuilder.equal(root.get("type"), type));
                }
                params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    private Specification<PhMerchant> getFilter(Object id,Object type) {
        return new Specification<PhMerchant>() {
            @Override
            public Predicate toPredicate(Root<PhMerchant> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (id != null){
                    params.add(criteriaBuilder.equal(root.get("id"), id));
                }
                if (type != null){
                    params.add(criteriaBuilder.equal(root.get("type"), type));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        };
    }

    @Override
    public Page<PhMerchant> findAll(Long id, Pageable pageable) {
        return phMerchantRepository.findAll(getFilter(id,null), pageable);
    }

    @Override
    public List<PhMerchant> findAll() {
        return phMerchantRepository.findAll();
    }

    @Override
    public List<PhMerchant> findAll(Long id) {
        return phMerchantRepository.findAll(getFilter(id,null));
    }

    @Override
    public void del(Long id) {
        phMerchantRepository.delete(id);
    }

    @Override
    public PhMerchant findByAdminId(Long adminId) {
        return phMerchantRepository.findOne(new Specification<PhMerchant>() {
            @Override
            public Predicate toPredicate(Root<PhMerchant> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("adminId"), adminId));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public PhMerchant findOne(Long id) {
        return phMerchantRepository.findOne(id);
    }

    @Override
    public void resetSort( Long sort) {
        phMerchantRepository.resort(sort);
    }

    @Override
    public List<PhMerchant> findAll(String type) {
        return phMerchantRepository.findAll(getFilter(null,type),new Sort("sort"));
    }
}
