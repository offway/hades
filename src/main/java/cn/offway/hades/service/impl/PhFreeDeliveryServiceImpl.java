package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhFreeDelivery;
import cn.offway.hades.repository.PhFreeDeliveryRepository;
import cn.offway.hades.service.PhFreeDeliveryService;
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
 * 免费送Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
@Service
public class PhFreeDeliveryServiceImpl implements PhFreeDeliveryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhFreeDeliveryRepository phFreeDeliveryRepository;

    @Override
    public PhFreeDelivery save(PhFreeDelivery phFreeDelivery) {
        return phFreeDeliveryRepository.save(phFreeDelivery);
    }

    @Override
    public PhFreeDelivery findOne(Long id) {
        return phFreeDeliveryRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phFreeDeliveryRepository.delete(id);
    }

    @Override
    public List<PhFreeDelivery> save(List<PhFreeDelivery> entities) {
        return phFreeDeliveryRepository.save(entities);
    }

    @Override
    public Page<PhFreeDelivery> findAll(Pageable pageable) {
        return phFreeDeliveryRepository.findAll(pageable);
    }

    @Override
    public void deleteByproductIdInList(List<Long> id) {
        phFreeDeliveryRepository.deleteByproductId(id);
    }

    @Override
    public List<PhFreeDelivery> findOneByProductId(Long id) {
        return phFreeDeliveryRepository.findAll(new Specification<PhFreeDelivery>() {
            @Override
            public Predicate toPredicate(Root<PhFreeDelivery> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("productId"), id));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public void deleteByproductId(Long id) {
        phFreeDeliveryRepository.deleteBypId(id);
    }
}
