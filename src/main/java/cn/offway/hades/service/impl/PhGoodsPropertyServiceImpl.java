package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhGoodsProperty;
import cn.offway.hades.repository.PhGoodsPropertyRepository;
import cn.offway.hades.service.PhGoodsPropertyService;
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
 * 商品属性Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsPropertyServiceImpl implements PhGoodsPropertyService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhGoodsPropertyRepository phGoodsPropertyRepository;

    @Override
    public PhGoodsProperty save(PhGoodsProperty phGoodsProperty) {
        return phGoodsPropertyRepository.save(phGoodsProperty);
    }

    @Override
    public Page<PhGoodsProperty> findAll(Pageable pageable) {
        return phGoodsPropertyRepository.findAll(pageable);
    }

    @Override
    public Page<PhGoodsProperty> findAll(String goodsId, Pageable pageable) {
        return phGoodsPropertyRepository.findAll(getFilter(goodsId), pageable);
    }

    @Override
    public List<PhGoodsProperty> findByPid(Long pid) {
        return phGoodsPropertyRepository.findAll(getFilter(pid));
    }

    private Specification<PhGoodsProperty> getFilter(Object goodsId) {
        return new Specification<PhGoodsProperty>() {
            @Override
            public Predicate toPredicate(Root<PhGoodsProperty> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("goodsId"), goodsId));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        };
    }

    @Override
    public List<PhGoodsProperty> findByStockId(Long sid) {
        return phGoodsPropertyRepository.findAll(new Specification<PhGoodsProperty>() {
            @Override
            public Predicate toPredicate(Root<PhGoodsProperty> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("goodsStockId"), sid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public void del(Long id) {
        phGoodsPropertyRepository.delete(id);
    }

    @Override
    public void delByPid(Long id) {
        phGoodsPropertyRepository.deleteByPid(id);
    }

    @Override
    public void delByStockId(Long sid) {
        phGoodsPropertyRepository.deleteByStockId(sid);
    }

    @Override
    public PhGoodsProperty findOne(Long id) {
        return phGoodsPropertyRepository.findOne(id);
    }
}
