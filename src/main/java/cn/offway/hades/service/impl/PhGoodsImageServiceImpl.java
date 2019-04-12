package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhGoodsImage;
import cn.offway.hades.repository.PhGoodsImageRepository;
import cn.offway.hades.service.PhGoodsImageService;
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
 * 商品图片Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsImageServiceImpl implements PhGoodsImageService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhGoodsImageRepository phGoodsImageRepository;

    @Override
    public PhGoodsImage save(PhGoodsImage phGoodsImage) {
        return phGoodsImageRepository.save(phGoodsImage);
    }

    @Override
    public Page<PhGoodsImage> findAll(Pageable pageable) {
        return phGoodsImageRepository.findAll(pageable);
    }

    @Override
    public Page<PhGoodsImage> findAll(String goodsId, Pageable pageable) {
        return phGoodsImageRepository.findAll(new Specification<PhGoodsImage>() {
            @Override
            public Predicate toPredicate(Root<PhGoodsImage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("goodsId"), goodsId));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public void del(Long id) {
        phGoodsImageRepository.delete(id);
    }

    @Override
    public void delByPid(Long id) {
        phGoodsImageRepository.deleteByPid(id);
    }

    @Override
    public PhGoodsImage findOne(Long id) {
        return phGoodsImageRepository.findOne(id);
    }
}
