package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhOrderGoods;
import cn.offway.hades.repository.PhOrderGoodsRepository;
import cn.offway.hades.service.PhOrderGoodsService;
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
 * 订单商品Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhOrderGoodsServiceImpl implements PhOrderGoodsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhOrderGoodsRepository phOrderGoodsRepository;

    @Override
    public PhOrderGoods save(PhOrderGoods phOrderGoods) {
        return phOrderGoodsRepository.save(phOrderGoods);
    }

    @Override
    public List<PhOrderGoods> findAllByPid(String orderNo) {
        return phOrderGoodsRepository.findAll(new Specification<PhOrderGoods>() {
            @Override
            public Predicate toPredicate(Root<PhOrderGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("orderNo"), orderNo));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public PhOrderGoods findOne(Long id) {
        return phOrderGoodsRepository.findOne(id);
    }
}
