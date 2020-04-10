package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhOrderInfo;
import cn.offway.hades.domain.PhRefund;
import cn.offway.hades.domain.PhUserInfo;
import cn.offway.hades.repository.PhOrderInfoRepository;
import cn.offway.hades.service.PhOrderInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
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
    public Iterable<PhOrderInfo> findAll(Long mid, String orderNo, Date sTime, Date eTime, String userId, String payMethod, String[] status, String channel, Pageable pageable) {
        Specification<PhOrderInfo> specification = new Specification<PhOrderInfo>() {
            @Override
            public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (mid != 0L) {
                    params.add(criteriaBuilder.equal(root.get("merchantId"), mid));
                }
                if (!"".equals(orderNo)) {
                    params.add(criteriaBuilder.like(root.get("orderNo"), "%" + orderNo + "%"));
                }
                if (sTime != null && eTime != null) {
                    params.add(criteriaBuilder.between(root.get("createTime"), sTime, eTime));
                } else if (sTime != null) {
                    params.add(criteriaBuilder.greaterThan(root.get("createTime"), sTime));
                } else if (eTime != null) {
                    params.add(criteriaBuilder.lessThan(root.get("createTime"), eTime));
                }
                if (!"".equals(userId)) {
                    params.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                if (!"".equals(payMethod)) {
                    params.add(criteriaBuilder.equal(root.get("payChannel"), payMethod));
                }
                if (status.length > 0) {
                    CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("status"));
                    boolean isOk = true;
                    for (String v : status) {
                        in.value(v);
                        isOk = "".equals(v.trim()) ? isOk && false : isOk && true;
                    }
                    if (isOk) {
                        params.add(in);
                    }
                }
                if (StringUtils.isNotBlank(channel)) {
                    Subquery<PhUserInfo> subquery = criteriaQuery.subquery(PhUserInfo.class);
                    Root<PhUserInfo> subRoot = subquery.from(PhUserInfo.class);
                    subquery.select(subRoot);
                    subquery.where(
                            criteriaBuilder.equal(root.get("userId"), subRoot.get("id")),
                            criteriaBuilder.equal(subRoot.get("channel"), channel)
                    );
                    params.add(criteriaBuilder.exists(subquery));
                    //排除售后订单
                    Subquery<PhRefund> subqueryIn = criteriaQuery.subquery(PhRefund.class);
                    Root<PhRefund> subRootIn = subqueryIn.from(PhRefund.class);
                    subqueryIn.select(subRootIn.get("orderNo"));
                    subqueryIn.where(
                            criteriaBuilder.equal(subRootIn.get("status"), 4)
                    );
                    params.add(criteriaBuilder.not(criteriaBuilder.in(root.get("orderNo")).value(subqueryIn)));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        };
        if (pageable == null) {
            return phOrderInfoRepository.findAll(specification);
        } else {
            return phOrderInfoRepository.findAll(specification, pageable);
        }
    }

    @Override
    public List<PhOrderInfo> findAll(Long mid, String orderNo, Date sTime, Date eTime, String userId, String payMethod, String[] status) {
        return null;
    }

    @Override
    public Page<PhOrderInfo> findAll(String pid, Pageable pageable) {
        return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
            @Override
            public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (!"".equals(pid)) {
                    params.add(criteriaBuilder.equal(root.get("preorderNo"), pid));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhOrderInfo> findToCheck(Date start, Date stop) {
        return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
            @Override
            public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                /* 状态[0-已下单,1-已付款,2-已发货,3-已收货,4-取消] */
                params.add(criteriaBuilder.equal(root.get("status"), "3"));
                params.add(criteriaBuilder.greaterThan(root.get("createTime"), start));
                params.add(criteriaBuilder.lessThan(root.get("receiptTime"), stop));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, new Sort("merchantId"));
    }

    @Override
    public List<PhOrderInfo> findToProcess(Date start, Date stop) {
        return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
            @Override
            public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                /* 状态[0-已下单,1-已付款,2-已发货,3-已收货,4-取消] */
                params.add(criteriaBuilder.equal(root.get("status"), "0"));
                params.add(criteriaBuilder.between(root.get("createTime"), start, stop));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<PhOrderInfo> findAll(String preOrderNo) {
        return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
            @Override
            public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                /* 状态[0-已下单,1-已付款,2-已发货,3-已收货,4-取消] */
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("status"));
                in.value("1");
                in.value("2");
                in.value("3");
                params.add(in);
                params.add(criteriaBuilder.equal(root.get("preorderNo"), preOrderNo));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<PhOrderInfo> findByPreorderNoAndStatus(String preOrderNo, String... status) {
        return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
            @Override
            public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (status != null && status.length > 0) {
                    /* 状态[0-已下单,1-已付款,2-已发货,3-已收货,4-取消] */
                    CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("status"));
                    for (String s : status) {
                        in.value(s);
                    }
                    params.add(in);
                }
                if (!"".equals(preOrderNo)) {
                    params.add(criteriaBuilder.equal(root.get("preorderNo"), preOrderNo));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public PhOrderInfo findOne(String orderNo) {
        return phOrderInfoRepository.findOne(new Specification<PhOrderInfo>() {
            @Override
            public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("orderNo"), orderNo));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<Object[]> stat(String sTime, String eTime) {
        if (StringUtils.isNotBlank(sTime) && StringUtils.isNotBlank(eTime)) {
            return phOrderInfoRepository.stat(sTime, eTime);
        } else if (StringUtils.isNotBlank(sTime)) {
            return phOrderInfoRepository.statGt(sTime);
        } else if (StringUtils.isNotBlank(eTime)) {
            return phOrderInfoRepository.statLt(eTime);
        } else {
            return phOrderInfoRepository.stat();
        }
    }

    @Override
    public PhOrderInfo findOne(Long id) {
        return phOrderInfoRepository.findOne(id);
    }
}
