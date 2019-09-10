package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhRefund;
import cn.offway.hades.domain.PhSettlementDetail;
import cn.offway.hades.repository.PhSettlementDetailRepository;
import cn.offway.hades.service.PhSettlementDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
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
    public Iterable<PhSettlementDetail> findAll(Long merchantId, Date sTime, Date eTime, String orderStatus, String status, String payChannel, Pageable pageable) {
        Specification<PhSettlementDetail> specification = new Specification<PhSettlementDetail>() {
            @Override
            public Predicate toPredicate(Root<PhSettlementDetail> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantId"), merchantId));
                if (!"".equals(status)) {
                    params.add(criteriaBuilder.equal(root.get("status"), status));
                }
                if (!"".equals(payChannel)) {
                    params.add(criteriaBuilder.equal(root.get("payChannel"), payChannel));
                }
                if (sTime != null && eTime != null) {
                    params.add(criteriaBuilder.between(root.get("createTime"), sTime, eTime));
                } else if (sTime != null) {
                    params.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), sTime));
                } else if (eTime != null) {
                    params.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), eTime));
                }
                if (!"".equals(orderStatus)) {
                    if ("5".equals(orderStatus)) {
                        Subquery<PhRefund> subquery = criteriaQuery.subquery(PhRefund.class);
                        Root<PhRefund> subRoot = subquery.from(PhRefund.class);
                        subquery.select(subRoot);
                        subquery.where(
                                criteriaBuilder.equal(root.get("orderNo"), subRoot.get("orderNo")),
                                criteriaBuilder.equal(subRoot.get("status"), "4")
                        );
                        Predicate altWay = criteriaBuilder.exists(subquery);
                        Predicate preferWay = criteriaBuilder.equal(root.get("remark"), orderStatus);
                        Predicate or = criteriaBuilder.or(preferWay, altWay);
                        params.add(or);
                    } else {
                        params.add(criteriaBuilder.equal(root.get("remark"), orderStatus));
                    }
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        };
        if (pageable != null) {
            return phSettlementDetailRepository.findAll(specification, pageable);
        } else {
            return phSettlementDetailRepository.findAll(specification);
        }
    }

    @Override
    public List<PhSettlementDetail> findList(Long[] idList) {
        return phSettlementDetailRepository.findAll(new Specification<PhSettlementDetail>() {
            @Override
            public Predicate toPredicate(Root<PhSettlementDetail> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (idList.length > 0) {
                    CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("id"));
                    for (Long v : idList) {
                        in.value(v);
                    }
                    params.add(in);
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public PhSettlementDetail findOne(Long id) {
        return phSettlementDetailRepository.findOne(id);
    }
}
