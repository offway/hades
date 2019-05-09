package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhPush;
import cn.offway.hades.repository.PhPushRepository;
import cn.offway.hades.service.PhPushService;
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
 * 推送记录Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPushServiceImpl implements PhPushService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhPushRepository phPushRepository;

    @Override
    public PhPush save(PhPush phPush) {
        return phPushRepository.save(phPush);
    }

    @Override
    public Page<PhPush> list(String name, String content, String type, Date sTime, Date eTime, Pageable pageable) {
        return phPushRepository.findAll(new Specification<PhPush>() {
            @Override
            public Predicate toPredicate(Root<PhPush> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (!"".equals(name)) {
                    params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }
                if (!"".equals(content)) {
                    params.add(criteriaBuilder.like(root.get("content"), "%" + content + "%"));
                }
                if (!"".equals(type)) {
                    params.add(criteriaBuilder.equal(root.get("type"), type));
                }
                if (sTime != null && eTime != null) {
                    params.add(criteriaBuilder.between(root.get("createTime"), sTime, eTime));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public void del(Long id) {
        phPushRepository.delete(id);
    }

    @Override
    public PhPush findOne(Long id) {
        return phPushRepository.findOne(id);
    }
}
