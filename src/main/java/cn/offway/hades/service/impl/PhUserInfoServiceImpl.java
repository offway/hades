package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhUserInfo;
import cn.offway.hades.repository.PhUserInfoRepository;
import cn.offway.hades.service.PhUserInfoService;
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
 * 用户信息Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhUserInfoServiceImpl implements PhUserInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhUserInfoRepository phUserInfoRepository;

    @Override
    public PhUserInfo save(PhUserInfo phUserInfo) {
        return phUserInfoRepository.save(phUserInfo);
    }

    @Override
    public Page<PhUserInfo> list(String phone, String nickname, String sex, Pageable pageable) {
        return phUserInfoRepository.findAll(new Specification<PhUserInfo>() {
            @Override
            public Predicate toPredicate(Root<PhUserInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (!"".equals(phone)) {
                    params.add(criteriaBuilder.like(root.get("phone"), "%" + phone + "%"));
                }
                if (!"".equals(nickname)) {
                    params.add(criteriaBuilder.like(root.get("nickname"), "%" + nickname + "%"));
                }
                if (!"".equals(sex)) {
                    params.add(criteriaBuilder.equal(root.get("sex"), sex));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public PhUserInfo findOne(Long id) {
        return phUserInfoRepository.findOne(id);
    }
}
