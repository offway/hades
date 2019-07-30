package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhCapitalFlow;
import cn.offway.hades.repository.PhCapitalFlowRepository;
import cn.offway.hades.service.PhCapitalFlowService;
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
 * 资金流水Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhCapitalFlowServiceImpl implements PhCapitalFlowService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhCapitalFlowRepository phCapitalFlowRepository;

    @Override
    public PhCapitalFlow save(PhCapitalFlow phCapitalFlow) {
        return phCapitalFlowRepository.save(phCapitalFlow);
    }

    @Override
    public PhCapitalFlow findOne(Long id) {
        return phCapitalFlowRepository.findOne(id);
    }

    @Override
    public List<PhCapitalFlow> finAllByuseridList(Long userid) {
        return phCapitalFlowRepository.findAll(new Specification<PhCapitalFlow>() {
            @Override
            public Predicate toPredicate(Root<PhCapitalFlow> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (null != userid) {
                    params.add(criteriaBuilder.equal(root.get("userId"), userid));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<PhCapitalFlow> finAllByamountFlowing() {
        return phCapitalFlowRepository.findAll(new Specification<PhCapitalFlow>() {
            @Override
            public Predicate toPredicate(Root<PhCapitalFlow> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> params = new ArrayList<Predicate>();
//                params.add(criteriaBuilder.equal(root.get("businessType"), "0"));
//                Predicate[] predicates = new Predicate[params.size()];
//                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }
}
