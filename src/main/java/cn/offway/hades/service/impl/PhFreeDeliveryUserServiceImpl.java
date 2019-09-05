package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhFreeDeliveryUser;
import cn.offway.hades.repository.PhFreeDeliveryUserRepository;
import cn.offway.hades.service.PhFreeDeliveryUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * 免费送参与用户Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
@Service
public class PhFreeDeliveryUserServiceImpl implements PhFreeDeliveryUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhFreeDeliveryUserRepository phFreeDeliveryUserRepository;

    @Override
    public PhFreeDeliveryUser save(PhFreeDeliveryUser phFreeDeliveryUser) {
        return phFreeDeliveryUserRepository.save(phFreeDeliveryUser);
    }

    @Override
    public PhFreeDeliveryUser findOne(Long id) {
        return phFreeDeliveryUserRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phFreeDeliveryUserRepository.delete(id);
    }

    @Override
    public Long getCountByPid(Long id) {
        return phFreeDeliveryUserRepository.count(new Specification<PhFreeDeliveryUser>() {
            @Override
            public Predicate toPredicate(Root<PhFreeDeliveryUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("freeDeliveryId"), id));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<PhFreeDeliveryUser> getListByPid(Long id) {
        return phFreeDeliveryUserRepository.findAll(new Specification<PhFreeDeliveryUser>() {
            @Override
            public Predicate toPredicate(Root<PhFreeDeliveryUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("freeDeliveryId"), id));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<PhFreeDeliveryUser> save(List<PhFreeDeliveryUser> entities) {
        return phFreeDeliveryUserRepository.save(entities);
    }
}
