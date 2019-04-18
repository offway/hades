package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.repository.PhGoodsRepository;
import cn.offway.hades.service.PhGoodsService;
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
 * 商品表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsServiceImpl implements PhGoodsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhGoodsRepository phGoodsRepository;

    @Override
    public PhGoods save(PhGoods phGoods) {
        return phGoodsRepository.save(phGoods);
    }

    @Override
    public Page<PhGoods> findAll(Pageable pageable) {
        return phGoodsRepository.findAll(pageable);
    }

    @Override
    public Page<PhGoods> findAll(String name, String code, Pageable pageable) {
        return phGoodsRepository.findAll(new Specification<PhGoods>() {
            @Override
            public Predicate toPredicate(Root<PhGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                params.add(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public void del(Long id) {
        phGoodsRepository.delete(id);
    }

    @Override
    public PhGoods findOne(Long id) {
        return phGoodsRepository.findOne(id);
    }
}
