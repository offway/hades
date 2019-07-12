package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhRefund;
import cn.offway.hades.repository.PhRefundRepository;
import cn.offway.hades.service.PhRefundService;
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
import java.util.Date;
import java.util.List;


/**
 * 退款/退货Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhRefundServiceImpl implements PhRefundService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhRefundRepository phRefundRepository;

    @Override
    public PhRefund save(PhRefund phRefund) {
        return phRefundRepository.save(phRefund);
    }

    private Specification<PhRefund> getFilter(String orderNo, Date sTime, Date eTime, String userId, Date sTimeCheck, Date eTimeCheck, String type, String[] status) {
        return new Specification<PhRefund>() {
            @Override
            public Predicate toPredicate(Root<PhRefund> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (!"".equals(orderNo)) {
                    params.add(criteriaBuilder.equal(root.get("orderNo"), orderNo));
                }
                if (!"".equals(userId)) {
                    params.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                if (!"".equals(type)) {
                    params.add(criteriaBuilder.equal(root.get("type"), type));
                }
                if (status != null && status.length > 0) {
                    CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("status"));
                    for (String v : status) {
                        in.value(v);
                    }
                    params.add(in);
                }
                if (sTime != null && eTime != null) {
                    params.add(criteriaBuilder.between(root.get("createTime"), sTime, eTime));
                } else if (sTime != null) {
                    params.add(criteriaBuilder.greaterThan(root.get("createTime"), sTime));
                } else if (eTime != null) {
                    params.add(criteriaBuilder.lessThan(root.get("createTime"), eTime));
                }
                if (sTimeCheck != null && eTimeCheck != null) {
                    params.add(criteriaBuilder.between(root.get("checkTime"), sTimeCheck, eTimeCheck));
                } else if (sTimeCheck != null) {
                    params.add(criteriaBuilder.greaterThan(root.get("checkTime"), sTimeCheck));
                } else if (eTimeCheck != null) {
                    params.add(criteriaBuilder.lessThan(root.get("checkTime"), eTimeCheck));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        };
    }

    @Override
    public PhRefund findOne(String orderNo) {
        return phRefundRepository.findOne(getFilter(orderNo, null, null, "", null, null, "", null));
    }

    @Override
    public PhRefund findOne(String orderNo, String... status) {
        return phRefundRepository.findOne(getFilter(orderNo, null, null, "", null, null, "", status));
    }

    @Override
    public Page<PhRefund> list(String orderNo, Date sTime, Date eTime, String userId, Date sTimeCheck, Date eTimeCheck, String type, String status, Pageable pageable) {
        if ("".equals(status)) {
            return phRefundRepository.findAll(getFilter(orderNo, sTime, eTime, userId, sTimeCheck, eTimeCheck, type, new String[]{}), pageable);
        } else {
            return phRefundRepository.findAll(getFilter(orderNo, sTime, eTime, userId, sTimeCheck, eTimeCheck, type, new String[]{status}), pageable);
        }
    }

    @Override
    public List<PhRefund> all(String orderNo, Date sTime, Date eTime, String userId, Date sTimeCheck, Date eTimeCheck, String type, String status) {
        if ("".equals(status)) {
            return phRefundRepository.findAll(getFilter(orderNo, sTime, eTime, userId, sTimeCheck, eTimeCheck, type, new String[]{}));
        } else {
            return phRefundRepository.findAll(getFilter(orderNo, sTime, eTime, userId, sTimeCheck, eTimeCheck, type, new String[]{status}));
        }
    }

    @Override
    public PhRefund findOne(Long id) {
        return phRefundRepository.findOne(id);
    }
}
