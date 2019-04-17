package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhMerchantFare;
import cn.offway.hades.repository.PhMerchantFareRepository;
import cn.offway.hades.service.PhMerchantFareService;
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
 * 商户运费模板Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhMerchantFareServiceImpl implements PhMerchantFareService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhMerchantFareRepository phMerchantFareRepository;

    @Override
    public PhMerchantFare save(PhMerchantFare phMerchantFare) {
        return phMerchantFareRepository.save(phMerchantFare);
    }

    @Override
    public List<PhMerchantFare> getByPid(Long pid) {
        return phMerchantFareRepository.findAll(new Specification<PhMerchantFare>() {
            @Override
            public Predicate toPredicate(Root<PhMerchantFare> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public Page<PhMerchantFare> getByPidPage(Long pid, Pageable pageable) {
        return phMerchantFareRepository.findAll(new Specification<PhMerchantFare>() {
            @Override
            public Predicate toPredicate(Root<PhMerchantFare> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<PhMerchantFare> getByPidPage(Long pid, String remark, Pageable pageable) {
        return phMerchantFareRepository.findAll(new Specification<PhMerchantFare>() {
            @Override
            public Predicate toPredicate(Root<PhMerchantFare> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantId"), pid));
                params.add(criteriaBuilder.like(root.get("remark"), "%" + remark + "%"));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public void del(Long id) {
        phMerchantFareRepository.delete(id);
    }

    @Override
    public void makeAllToUnDefault(Long pid) {
        phMerchantFareRepository.makeAllToUnDefault(pid);
    }

    @Override
    public PhMerchantFare findOne(Long id) {
        return phMerchantFareRepository.findOne(id);
    }
}
