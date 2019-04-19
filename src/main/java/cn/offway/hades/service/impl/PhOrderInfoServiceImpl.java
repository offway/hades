package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhOrderInfo;
import cn.offway.hades.repository.PhOrderInfoRepository;
import cn.offway.hades.service.PhOrderInfoService;
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
 * 订单Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhOrderInfoServiceImpl implements PhOrderInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhOrderInfoRepository phOrderInfoRepository;

    @Override
    public PhOrderInfo save(PhOrderInfo phOrderInfo) {
        return phOrderInfoRepository.save(phOrderInfo);
    }

    @Override
    public Page<PhOrderInfo> findAll(Long mid, String orderNo, String sTime, String eTime, String userId, String payMethod, String status, Pageable pageable) {
        return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
            @Override
            public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (mid != 0L) {
                    params.add(criteriaBuilder.equal(root.get("merchantId"), mid));
                }
                if (!"".equals(orderNo)) {
                    params.add(criteriaBuilder.like(root.get("orderNo"), "%" + orderNo + "%"));
                }
                if (!"".equals(sTime) && !"".equals(eTime)) {
                    params.add(criteriaBuilder.between(root.get("createTime"), sTime, eTime));
                }
                if (!"".equals(userId)) {
                    params.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                if (!"".equals(payMethod)) {
                    params.add(criteriaBuilder.equal(root.get("payChannel"), payMethod));
                }
                if (!"".equals(status)) {
                    params.add(criteriaBuilder.equal(root.get("status"), status));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public PhOrderInfo findOne(Long id) {
        return phOrderInfoRepository.findOne(id);
    }
}
