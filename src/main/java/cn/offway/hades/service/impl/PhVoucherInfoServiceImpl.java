package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhVoucherInfo;
import cn.offway.hades.repository.PhVoucherInfoRepository;
import cn.offway.hades.service.PhVoucherInfoService;
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
 * 优惠券Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhVoucherInfoServiceImpl implements PhVoucherInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhVoucherInfoRepository phVoucherInfoRepository;

    @Override
    public PhVoucherInfo save(PhVoucherInfo phVoucherInfo) {
        return phVoucherInfoRepository.save(phVoucherInfo);
    }

    @Override
    public void delByPid(Long pid) {
        phVoucherInfoRepository.deleteByPid(pid);
    }

    @Override
    public List<PhVoucherInfo> getByPid(Long pid) {
        return phVoucherInfoRepository.findAll(new Specification<PhVoucherInfo>() {
            @Override
            public Predicate toPredicate(Root<PhVoucherInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("voucherProjectId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public PhVoucherInfo findOne(Long id) {
        return phVoucherInfoRepository.findOne(id);
    }
}
