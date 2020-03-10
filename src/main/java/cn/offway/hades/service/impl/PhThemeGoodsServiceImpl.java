package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhThemeGoods;
import cn.offway.hades.repository.PhThemeGoodsRepository;
import cn.offway.hades.service.PhThemeGoodsService;
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


/**
 * 主题商品表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
@Service
public class PhThemeGoodsServiceImpl implements PhThemeGoodsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhThemeGoodsRepository phThemeGoodsRepository;

    @Override
    public PhThemeGoods save(PhThemeGoods phThemeGoods) {
        return phThemeGoodsRepository.save(phThemeGoods);
    }

    @Override
    public PhThemeGoods findOne(Long id) {
        return phThemeGoodsRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phThemeGoodsRepository.delete(id);
    }

    @Override
    public List<PhThemeGoods> findByPid(Long pid) {
        return phThemeGoodsRepository.findAll(new Specification<PhThemeGoods>() {
            @Override
            public Predicate toPredicate(Root<PhThemeGoods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(cb.equal(root.get("themeId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        }, new Sort(new Sort.Order(Sort.Direction.ASC, "id")));
    }

    @Override
    public void delByPid(Long pid) {
        phThemeGoodsRepository.deleteByPid(pid);
    }

    @Override
    public List<PhThemeGoods> save(List<PhThemeGoods> entities) {
        return phThemeGoodsRepository.save(entities);
    }
}
