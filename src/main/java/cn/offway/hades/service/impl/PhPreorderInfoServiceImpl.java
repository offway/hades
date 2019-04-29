package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhPreorderInfo;
import cn.offway.hades.repository.PhPreorderInfoRepository;
import cn.offway.hades.service.PhPreorderInfoService;
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
 * 预生成订单Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPreorderInfoServiceImpl implements PhPreorderInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhPreorderInfoRepository phPreorderInfoRepository;

    @Override
    public PhPreorderInfo save(PhPreorderInfo phPreorderInfo) {
        return phPreorderInfoRepository.save(phPreorderInfo);
    }

    @Override
    public PhPreorderInfo findOne(Long id) {
        return phPreorderInfoRepository.findOne(id);
    }

    @Override
    public PhPreorderInfo findByOrderNoAndStatus(String orderno, String status) {
        return phPreorderInfoRepository.findByOrderNoAndStatus(orderno, status);
    }

    @Override
    public int countByUserIdAndStatus(Long userId, String status) {
        return phPreorderInfoRepository.countByUserIdAndStatus(userId, status);
    }

    @Override
    public Page<PhPreorderInfo> findAll(String orderNo, Date sTime, Date eTime, String userId, String payMethod, String status, Pageable pageable) {
        return phPreorderInfoRepository.findAll(new Specification<PhPreorderInfo>() {
            @Override
            public Predicate toPredicate(Root<PhPreorderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (!"".equals(orderNo)) {
                    params.add(criteriaBuilder.like(root.get("orderNo"), "%" + orderNo + "%"));
                }
                if (sTime != null && eTime != null) {
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
}
