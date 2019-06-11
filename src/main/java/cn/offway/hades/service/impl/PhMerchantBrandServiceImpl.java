package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.domain.PhMerchantBrand;
import cn.offway.hades.repository.PhMerchantBrandRepository;
import cn.offway.hades.service.PhMerchantBrandService;
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
 * 商户品牌关系Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhMerchantBrandServiceImpl implements PhMerchantBrandService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhMerchantBrandRepository phMerchantBrandRepository;

    @Override
    public PhMerchantBrand save(PhMerchantBrand phMerchantBrand) {
        return phMerchantBrandRepository.save(phMerchantBrand);
    }

    @Override
    public List<PhMerchantBrand> findByPid(Long pid) {
        return phMerchantBrandRepository.findAll(new Specification<PhMerchantBrand>() {
            @Override
            public Predicate toPredicate(Root<PhMerchantBrand> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public void updateMerchantInfo(PhMerchant merchant) {
        phMerchantBrandRepository.updateMerchantInfo(merchant.getId(), merchant.getLogo(), merchant.getName());
    }

    @Override
    public void updateBrandInfo(PhBrand brand) {
        phMerchantBrandRepository.updateBrandInfo(brand.getId(), brand.getLogo(), brand.getName());
    }

    @Override
    public List<PhMerchantBrand> findByBid(Long bid) {
        return phMerchantBrandRepository.findAll(new Specification<PhMerchantBrand>() {
            @Override
            public Predicate toPredicate(Root<PhMerchantBrand> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("brandId"), bid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public void del(Long id) {
        phMerchantBrandRepository.delete(id);
    }

    @Override
    public void delByPid(Long pid) {
        phMerchantBrandRepository.deleteByPid(pid);
    }

    @Override
    public PhMerchantBrand findOne(Long id) {
        return phMerchantBrandRepository.findOne(id);
    }
}
