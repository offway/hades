package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhVoucherProject;
import cn.offway.hades.repository.PhVoucherProjectRepository;
import cn.offway.hades.service.PhVoucherProjectService;
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
import java.util.Date;
import java.util.List;


/**
 * 优惠券方案Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhVoucherProjectServiceImpl implements PhVoucherProjectService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhVoucherProjectRepository phVoucherProjectRepository;

    @Override
    public PhVoucherProject save(PhVoucherProject phVoucherProject) {
        return phVoucherProjectRepository.save(phVoucherProject);
    }

    @Override
    public Page<PhVoucherProject> list(String type, String name, Long merchantId, Date beginTime, Date endTime, String remark, String isPrivate, Pageable pageable) {
        return phVoucherProjectRepository.findAll(new Specification<PhVoucherProject>() {
            @Override
            public Predicate toPredicate(Root<PhVoucherProject> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (!"".equals(type)) {
                    params.add(criteriaBuilder.equal(root.get("type"), type));
                }
                if (!"".equals(isPrivate)) {
                    params.add(criteriaBuilder.equal(root.get("isPrivate"), isPrivate));
                }
                if (!"".equals(name)) {
                    params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }
                if (merchantId != 0L) {
                    params.add(criteriaBuilder.equal(root.get("merchantId"), merchantId));
                }
                if (beginTime != null && endTime != null) {
                    params.add(criteriaBuilder.between(root.get("createTime"), beginTime, endTime));
                } else if (beginTime != null) {
                    params.add(criteriaBuilder.greaterThan(root.get("createTime"), beginTime));
                } else if (endTime != null) {
                    params.add(criteriaBuilder.lessThan(root.get("createTime"), endTime));
                }
                if (!"".equals(remark)) {
                    params.add(criteriaBuilder.like(root.get("remark"), "%" + remark + "%"));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public void del(Long id) {
        phVoucherProjectRepository.delete(id);
    }

    @Override
    public PhVoucherProject findOne(Long id) {
        return phVoucherProjectRepository.findOne(id);
    }
}
