package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhSettlementDetail;
import cn.offway.hades.repository.PhSettlementDetailRepository;
import cn.offway.hades.service.PhSettlementDetailService;
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
import java.util.Optional;


/**
 * 商户结算明细表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhSettlementDetailServiceImpl implements PhSettlementDetailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhSettlementDetailRepository phSettlementDetailRepository;

    @Override
    public PhSettlementDetail save(PhSettlementDetail phSettlementDetail) {
        return phSettlementDetailRepository.save(phSettlementDetail);
    }

    @Override
    public PhSettlementDetail findOne(String orderNo) {
        return null;
    }

    @Override
    public Boolean isExist(String orderNo) {
        Optional<String> res = phSettlementDetailRepository.getCount(orderNo);
        if (res.isPresent()) {
            return Integer.valueOf(String.valueOf(res.get())) > 0;
        } else {
            return false;
        }
    }

    @Override
    public Page<PhSettlementDetail> findAll(Long merchantId, Pageable pageable) {
        return phSettlementDetailRepository.findAll(new Specification<PhSettlementDetail>() {
            @Override
            public Predicate toPredicate(Root<PhSettlementDetail> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantId"), merchantId));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public PhSettlementDetail findOne(Long id) {
        return phSettlementDetailRepository.findOne(id);
    }
}
