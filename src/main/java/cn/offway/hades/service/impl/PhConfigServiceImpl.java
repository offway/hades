package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhConfig;
import cn.offway.hades.repository.PhConfigRepository;
import cn.offway.hades.service.PhConfigService;
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
 * 配置Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhConfigServiceImpl implements PhConfigService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhConfigRepository phConfigRepository;

    @Override
    public PhConfig save(PhConfig phConfig) {
        return phConfigRepository.save(phConfig);
    }

    @Override
    public PhConfig findOne(Long id) {
        return phConfigRepository.findOne(id);
    }

    @Override
    public PhConfig findOne(String name) {
        return phConfigRepository.findOne(new Specification<PhConfig>() {
            @Override
            public Predicate toPredicate(Root<PhConfig> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("name"), name));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public String findContentByName(String name) {
        return phConfigRepository.findContentByName(name);
    }
}
