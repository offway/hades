package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhMerchantFareSpecial;
import cn.offway.hades.repository.PhMerchantFareSpecialRepository;
import cn.offway.hades.service.PhMerchantFareSpecialService;
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
 * 商户运费特殊表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhMerchantFareSpecialServiceImpl implements PhMerchantFareSpecialService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhMerchantFareSpecialRepository phMerchantFareSpecialRepository;

    @Override
    public PhMerchantFareSpecial save(PhMerchantFareSpecial phMerchantFareSpecial) {
        return phMerchantFareSpecialRepository.save(phMerchantFareSpecial);
    }

    @Override
    public List<PhMerchantFareSpecial> getByPid(Long pid) {
        return phMerchantFareSpecialRepository.findAll(new Specification<PhMerchantFareSpecial>() {
            @Override
            public Predicate toPredicate(Root<PhMerchantFareSpecial> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantFareId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public Page<PhMerchantFareSpecial> getByPidPage(Long pid, Pageable pageable) {
        return phMerchantFareSpecialRepository.findAll(new Specification<PhMerchantFareSpecial>() {
            @Override
            public Predicate toPredicate(Root<PhMerchantFareSpecial> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantFareId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<PhMerchantFareSpecial> getByPidPage(Long pid, String remark, Pageable pageable) {
        return phMerchantFareSpecialRepository.findAll(new Specification<PhMerchantFareSpecial>() {
            @Override
            public Predicate toPredicate(Root<PhMerchantFareSpecial> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantFareId"), pid));
                params.add(criteriaBuilder.like(root.get("remark"), "%" + remark + "%"));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public void del(Long id) {
        phMerchantFareSpecialRepository.delete(id);
    }

    @Override
    public void delByPid(Long pid) {
        phMerchantFareSpecialRepository.deleteByPid(pid);
    }

    @Override
    public PhMerchantFareSpecial findOne(Long id) {
        return phMerchantFareSpecialRepository.findOne(id);
    }
}
