package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhRefundGoods;
import cn.offway.hades.repository.PhRefundGoodsRepository;
import cn.offway.hades.service.PhRefundGoodsService;
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
 * 退款/退货商品明细Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhRefundGoodsServiceImpl implements PhRefundGoodsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhRefundGoodsRepository phRefundGoodsRepository;

    @Override
    public PhRefundGoods save(PhRefundGoods phRefundGoods) {
        return phRefundGoodsRepository.save(phRefundGoods);
    }

    @Override
    public List<PhRefundGoods> listByPid(Long refundId) {
        return phRefundGoodsRepository.findAll(new Specification<PhRefundGoods>() {
            @Override
            public Predicate toPredicate(Root<PhRefundGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("refundId"), refundId));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public PhRefundGoods findOne(Long id) {
        return phRefundGoodsRepository.findOne(id);
    }
}
