package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhPickGoods;
import cn.offway.hades.repository.PhPickGoodsRepository;
import cn.offway.hades.service.PhPickGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
 * OFFWAY优选商品明细Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPickGoodsServiceImpl implements PhPickGoodsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhPickGoodsRepository phPickGoodsRepository;

    @Override
    public PhPickGoods save(PhPickGoods phPickGoods) {
        return phPickGoodsRepository.save(phPickGoods);
    }

    @Override
    public List<PhPickGoods> findByPid(Long pid) {
        return phPickGoodsRepository.findAll(new Specification<PhPickGoods>() {
            @Override
            public Predicate toPredicate(Root<PhPickGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("pickId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, new Sort(new Sort.Order(Sort.Direction.ASC, "id")));
    }

    @Override
    public List<Integer> findAllRestByPid(Long pid) {
        Optional<List<Integer>> res = phPickGoodsRepository.findAllRestByPid(pid);
        if (res.isPresent()) {
            return res.get();
        } else {
            return null;
        }
    }

    @Override
    public List<Integer> findAllRestGoods() {
        Optional<List<Integer>> res = phPickGoodsRepository.findAllRestGoods();
        if (res.isPresent()) {
            return res.get();
        } else {
            return null;
        }
    }

    @Override
    public void delByPid(Long pid) {
        phPickGoodsRepository.deleteByPid(pid);
    }

    @Override
    public PhPickGoods findOne(Long id) {
        return phPickGoodsRepository.findOne(id);
    }
}
