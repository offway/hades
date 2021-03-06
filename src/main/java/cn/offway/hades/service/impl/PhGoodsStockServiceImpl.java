package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.domain.PhGoodsStock;
import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.repository.PhGoodsStockRepository;
import cn.offway.hades.service.PhGoodsStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * 商品库存Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsStockServiceImpl implements PhGoodsStockService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhGoodsStockRepository phGoodsStockRepository;

    @Override
    public PhGoodsStock save(PhGoodsStock phGoodsStock) {
        return phGoodsStockRepository.save(phGoodsStock);
    }

    @Override
    public Page<PhGoodsStock> findAll(Pageable pageable) {
        return phGoodsStockRepository.findAll(pageable);
    }

    @Override
    public Page<PhGoodsStock> findAll(String goodsId, Pageable pageable) {
        return phGoodsStockRepository.findAll(getFilter(goodsId), pageable);
    }

    private Specification<PhGoodsStock> getFilter(Object id) {
        return new Specification<PhGoodsStock>() {
            @Override
            public Predicate toPredicate(Root<PhGoodsStock> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("goodsId"), id));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        };
    }

    @Override
    public Page<PhGoodsStock> findAll(String goodsId, String remark, Pageable pageable) {
        return phGoodsStockRepository.findAll(new Specification<PhGoodsStock>() {
            @Override
            public Predicate toPredicate(Root<PhGoodsStock> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("goodsId"), goodsId));
                params.add(criteriaBuilder.like(root.get("remark"), "%" + remark + "%"));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhGoodsStock> findByPid(Long pid) {
        return phGoodsStockRepository.findAll(getFilter(pid));
    }

    @Override
    public void updateByPid(Long id, Double value) {
        phGoodsStockRepository.updateByPid(id, value);
    }

    @Override
    public void updateMerchantInfo(PhMerchant merchant) {
        phGoodsStockRepository.updateMerchantInfo(merchant.getId(), merchant.getLogo(), merchant.getName());
    }

    @Override
    public void updateBrandInfo(PhBrand brand) {
        phGoodsStockRepository.updateBrandInfo(brand.getId(), brand.getLogo(), brand.getName());
    }

    @Override
    public List<PhGoodsStock> findByPids(Long[] ids) {
        return phGoodsStockRepository.findAll(new Specification<PhGoodsStock>() {
            @Override
            public Predicate toPredicate(Root<PhGoodsStock> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (ids != null && ids.length > 0) {
                    CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("goodsId"));
                    for (long id : ids) {
                        in.value(id);
                    }
                    params.add(in);
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, new Sort("goodsId"));
    }

    @Override
    public void del(Long id) {
        phGoodsStockRepository.delete(id);
    }

    @Override
    public void delByPid(Long id) {
        phGoodsStockRepository.deleteByPid(id);
    }

    @Override
    public PhGoodsStock findOne(Long id) {
        return phGoodsStockRepository.findOne(id);
    }
}
