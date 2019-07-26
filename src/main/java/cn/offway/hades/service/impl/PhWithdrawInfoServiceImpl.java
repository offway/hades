package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhWithdrawInfo;
import cn.offway.hades.repository.PhWithdrawInfoRepository;
import cn.offway.hades.service.PhWithdrawInfoService;
import org.apache.commons.lang3.StringUtils;
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
 * 提现订单Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-07-25 13:22:40 Exp $
 */
@Service
public class PhWithdrawInfoServiceImpl implements PhWithdrawInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhWithdrawInfoRepository phWithdrawInfoRepository;

    @Override
    public PhWithdrawInfo save(PhWithdrawInfo phWithdrawInfo) {
        return phWithdrawInfoRepository.save(phWithdrawInfo);
    }

    @Override
    public PhWithdrawInfo findOne(Long id) {
        return phWithdrawInfoRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phWithdrawInfoRepository.delete(id);
    }

    @Override
    public List<PhWithdrawInfo> save(List<PhWithdrawInfo> entities) {
        return phWithdrawInfoRepository.save(entities);
    }

    @Override
    public Page<PhWithdrawInfo> findAll(Pageable pageable) {
        return phWithdrawInfoRepository.findAll(pageable);
    }

    @Override
    public Page<PhWithdrawInfo> findAll(String userId, Double miniamount, Double maxamount, String status, Date startTime, Date endTime, Pageable pageable) {
        return phWithdrawInfoRepository.findAll(new Specification<PhWithdrawInfo>() {
            @Override
            public Predicate toPredicate(Root<PhWithdrawInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(userId)) {
                    params.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                if (miniamount!=null && maxamount!=null) {
                    params.add(criteriaBuilder.between(root.get("amount"), miniamount, maxamount));
                }
                if (StringUtils.isNotBlank(status)) {
                    params.add(criteriaBuilder.equal(root.get("status"), status));
                }
                if (startTime!=null && endTime!=null) {
                    params.add(criteriaBuilder.between(root.get("createTime"), startTime, endTime));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }
}
