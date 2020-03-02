package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhFollow;
import cn.offway.hades.repository.PhFollowRepository;
import cn.offway.hades.service.PhFollowService;
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
 * 关注列表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
@Service
public class PhFollowServiceImpl implements PhFollowService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhFollowRepository phFollowRepository;

    @Override
    public PhFollow save(PhFollow phFollow) {
        return phFollowRepository.save(phFollow);
    }

    @Override
    public PhFollow findOne(Long id) {
        return phFollowRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phFollowRepository.delete(id);
    }

    @Override
    public Page<PhFollow> list(long pid, Pageable pageable) {
        return phFollowRepository.findAll(new Specification<PhFollow>() {
            @Override
            public Predicate toPredicate(Root<PhFollow> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(cb.equal(root.get("celebrityId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhFollow> save(List<PhFollow> entities) {
        return phFollowRepository.save(entities);
    }
}
