package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhSettlementInfo;
import cn.offway.hades.repository.PhSettlementInfoRepository;
import cn.offway.hades.service.PhSettlementInfoService;
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
 * 商户结算表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhSettlementInfoServiceImpl implements PhSettlementInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhSettlementInfoRepository phSettlementInfoRepository;

    @Override
    public PhSettlementInfo save(PhSettlementInfo phSettlementInfo) {
        return phSettlementInfoRepository.save(phSettlementInfo);
    }

    @Override
    public PhSettlementInfo findByPid(Long merchantId) {
        return phSettlementInfoRepository.findOne(new Specification<PhSettlementInfo>() {
            @Override
            public Predicate toPredicate(Root<PhSettlementInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantId"), merchantId));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public Page<PhSettlementInfo> findAll(Pageable pageable) {
        return phSettlementInfoRepository.findAll(pageable);
    }

    @Override
    public Boolean isExist(Long merchantId) {
        Optional<String> res = phSettlementInfoRepository.getCount(merchantId);
        if (res.isPresent()) {
            return Integer.valueOf(String.valueOf(res.get())) > 0;
        } else {
            return false;
        }
    }

    @Override
    public PhSettlementInfo findOne(Long id) {
        return phSettlementInfoRepository.findOne(id);
    }
}
