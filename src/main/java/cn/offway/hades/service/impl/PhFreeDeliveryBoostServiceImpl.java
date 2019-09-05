package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhFreeDeliveryBoost;
import cn.offway.hades.domain.PhFreeDeliveryUser;
import cn.offway.hades.repository.PhFreeDeliveryBoostRepository;
import cn.offway.hades.service.PhFreeDeliveryBoostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 免费送助力Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
@Service
public class PhFreeDeliveryBoostServiceImpl implements PhFreeDeliveryBoostService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhFreeDeliveryBoostRepository phFreeDeliveryBoostRepository;

    @Override
    public PhFreeDeliveryBoost save(PhFreeDeliveryBoost phFreeDeliveryBoost) {
        return phFreeDeliveryBoostRepository.save(phFreeDeliveryBoost);
    }

    @Override
    public PhFreeDeliveryBoost findOne(Long id) {
        return phFreeDeliveryBoostRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phFreeDeliveryBoostRepository.delete(id);
    }

    @Override
    public Long getCountByPid(Long id) {
        return phFreeDeliveryBoostRepository.count(new Specification<PhFreeDeliveryBoost>() {
            @Override
            public Predicate toPredicate(Root<PhFreeDeliveryBoost> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                Subquery<PhFreeDeliveryUser> subQuery = criteriaQuery.subquery(PhFreeDeliveryUser.class);
                Root<PhFreeDeliveryUser> subRoot = subQuery.from(PhFreeDeliveryUser.class);
                subQuery.select(subRoot);
                subQuery.where(
                        criteriaBuilder.equal(root.get("freeDeliveryUserId"), subRoot.get("id")),
                        criteriaBuilder.equal(subRoot.get("freeDeliveryId"), id)
                );
                params.add(criteriaBuilder.exists(subQuery));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<PhFreeDeliveryBoost> getListByPid(Long id) {
        return phFreeDeliveryBoostRepository.findAll(new Specification<PhFreeDeliveryBoost>() {
            @Override
            public Predicate toPredicate(Root<PhFreeDeliveryBoost> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("freeDeliveryUserId"), id));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<PhFreeDeliveryBoost> save(List<PhFreeDeliveryBoost> entities) {
        return phFreeDeliveryBoostRepository.save(entities);
    }
}
