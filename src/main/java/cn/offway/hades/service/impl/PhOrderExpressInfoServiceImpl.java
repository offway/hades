package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhOrderExpressInfo;
import cn.offway.hades.repository.PhOrderExpressInfoRepository;
import cn.offway.hades.service.PhOrderExpressInfoService;
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
 * 订单物流Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhOrderExpressInfoServiceImpl implements PhOrderExpressInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhOrderExpressInfoRepository phOrderExpressInfoRepository;

    @Override
    public PhOrderExpressInfo save(PhOrderExpressInfo phOrderExpressInfo) {
        return phOrderExpressInfoRepository.save(phOrderExpressInfo);
    }

    @Override
    public PhOrderExpressInfo findByPid(String orderNo, String type) {
        return phOrderExpressInfoRepository.findOne(new Specification<PhOrderExpressInfo>() {
            @Override
            public Predicate toPredicate(Root<PhOrderExpressInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("orderNo"), orderNo));
                params.add(criteriaBuilder.equal(root.get("type"), type));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public PhOrderExpressInfo findOne(Long id) {
        return phOrderExpressInfoRepository.findOne(id);
    }
}
