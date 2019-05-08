package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhPick;
import cn.offway.hades.repository.PhPickRepository;
import cn.offway.hades.service.PhPickService;
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
 * OFFWAY优选Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhPickServiceImpl implements PhPickService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhPickRepository phPickRepository;

    @Override
    public PhPick save(PhPick phPick) {
        return phPickRepository.save(phPick);
    }

    @Override
    public Page<PhPick> list(Long id, String name, Date sTime, Date eTime, Pageable pageable) {
        return phPickRepository.findAll(new Specification<PhPick>() {
            @Override
            public Predicate toPredicate(Root<PhPick> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (id != 0L) {
                    params.add(criteriaBuilder.equal(root.get("id"), id));
                }
                if (!"".equals(name)) {
                    params.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
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
        phPickRepository.delete(id);
    }

    @Override
    public PhPick findOne(Long id) {
        return phPickRepository.findOne(id);
    }
}
