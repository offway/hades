package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.domain.PhMerchant;
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
import java.util.Optional;


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
    public Page<PhGoods> findAll(String name, Long id, String code, String status, Long merchantId, String merchantBrandId, String type, String category, Long[] gidArr, boolean inOrNot, Pageable pageable) {
        return phGoodsRepository.findAll(new Specification<PhGoods>() {
            @Override
            public Predicate toPredicate(Root<PhGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                params.add(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
                if (gidArr != null && gidArr.length > 0) {
                    CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("id"));
                    for (long id : gidArr) {
                        in.value(id);
                    }
                    if (inOrNot) {
                        params.add(in);
                    } else {
                        params.add(in.not());
                    }
                }
                if (!"".equals(status)) {
                    params.add(criteriaBuilder.equal(root.get("status"), status));
                }
                if (merchantId != 0L) {
                    params.add(criteriaBuilder.equal(root.get("merchantId"), merchantId));
                }
                if (!"".equals(merchantBrandId)) {
                    params.add(criteriaBuilder.equal(root.get("brandId"), merchantBrandId));
                }
                if (id != 0L) {
                    params.add(criteriaBuilder.equal(root.get("id"), id));
                }
                if (!"".equals(type)) {
                    params.add(criteriaBuilder.equal(root.get("type"), type));
                }
                if (!"".equals(category)) {
                    params.add(criteriaBuilder.equal(root.get("category"), category));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhGoods> findAll(String name, Object value) {
        return phGoodsRepository.findAll(new Specification<PhGoods>() {
            @Override
            public Predicate toPredicate(Root<PhGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("status"), "1"));
                params.add(criteriaBuilder.equal(root.get(name), value));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public Long getCountByPid(Long merchantId) {
        Optional<String> res = phGoodsRepository.getCount(merchantId);
        if (res.isPresent()) {
            return Long.valueOf(String.valueOf(res.get()));
        } else {
            return 0L;
        }
    }

    @Override
    public void updateMerchantInfo(PhMerchant merchant) {
        phGoodsRepository.updateMerchantInfo(merchant.getId(), merchant.getLogo(), merchant.getName(), merchant.getType());
    }

    @Override
    public void updateBrandInfo(PhBrand brand) {
        phGoodsRepository.updateBrandInfo(brand.getId(), brand.getLogo(), brand.getName());
    }

    @Override
    public void del(Long id) {
        phGoodsRepository.delete(id);
    }

    @Override
    public List<PhGoods> findByIds(Long[] ids) {
        return phGoodsRepository.findAll(new Specification<PhGoods>() {
            @Override
            public Predicate toPredicate(Root<PhGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (ids != null && ids.length > 0) {
                    CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("id"));
                    for (long id : ids) {
                        in.value(id);
                    }
                    params.add(in);
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<PhGoods> findAll(String mid, String bid) {
        Optional<List<PhGoods>> res;
        if ("".equals(mid)) {
            res = phGoodsRepository.findAllRestGoods(Long.valueOf(bid));
        } else {
            res = phGoodsRepository.findAllRestGoods(Long.valueOf(mid), Long.valueOf(bid));
        }
        return res.isPresent() ? res.get() : null;
    }

    @Override
    public PhGoods findOne(Long id) {
        return phGoodsRepository.findOne(id);
    }
}
