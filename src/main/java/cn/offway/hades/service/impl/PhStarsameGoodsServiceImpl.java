package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhStarsameGoods;
import cn.offway.hades.repository.PhStarsameGoodsRepository;
import cn.offway.hades.service.PhStarsameGoodsService;
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
 * 明星同款商品Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhStarsameGoodsServiceImpl implements PhStarsameGoodsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhStarsameGoodsRepository phStarsameGoodsRepository;

    @Override
    public PhStarsameGoods save(PhStarsameGoods phStarsameGoods) {
        return phStarsameGoodsRepository.save(phStarsameGoods);
    }

    @Override
    public void deleteByPid(Long pid) {
        phStarsameGoodsRepository.deleteByPid(pid);
    }

    @Override
    public List<PhStarsameGoods> findAllByPid(Long pid) {
        return phStarsameGoodsRepository.findAll(new Specification<PhStarsameGoods>() {
            @Override
            public Predicate toPredicate(Root<PhStarsameGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("starsameId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public PhStarsameGoods findOne(Long id) {
        return phStarsameGoodsRepository.findOne(id);
    }
}
