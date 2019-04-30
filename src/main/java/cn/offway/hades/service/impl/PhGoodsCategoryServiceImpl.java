package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhGoodsCategory;
import cn.offway.hades.repository.PhGoodsCategoryRepository;
import cn.offway.hades.service.PhGoodsCategoryService;
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
 * 商品类目Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsCategoryServiceImpl implements PhGoodsCategoryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhGoodsCategoryRepository phGoodsCategoryRepository;

    @Override
    public PhGoodsCategory save(PhGoodsCategory phGoodsCategory) {
        return phGoodsCategoryRepository.save(phGoodsCategory);
    }

    @Override
    public Page<PhGoodsCategory> findAll(Pageable pageable) {
        return phGoodsCategoryRepository.findAll(pageable);
    }

    @Override
    public void del(Long id) {
        phGoodsCategoryRepository.delete(id);
    }

    @Override
    public List<PhGoodsCategory> findByPid(Long pid) {
        return phGoodsCategoryRepository.findAll(new Specification<PhGoodsCategory>() {
            @Override
            public Predicate toPredicate(Root<PhGoodsCategory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("goodsType"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public Page<PhGoodsCategory> findByPid(Long pid, Pageable pageable) {
        return phGoodsCategoryRepository.findAll(new Specification<PhGoodsCategory>() {
            @Override
            public Predicate toPredicate(Root<PhGoodsCategory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("goodsType"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhGoodsCategory> findAll() {
        return phGoodsCategoryRepository.findAll();
    }

    @Override
    public void delByPid(Long pid) {
        phGoodsCategoryRepository.deleteByPid(pid);
    }

    @Override
    public PhGoodsCategory findOne(Long id) {
        return phGoodsCategoryRepository.findOne(id);
    }
}
