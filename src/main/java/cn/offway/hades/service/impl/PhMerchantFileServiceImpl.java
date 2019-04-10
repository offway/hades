package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhMerchantFile;
import cn.offway.hades.repository.PhMerchantFileRepository;
import cn.offway.hades.service.PhMerchantFileService;
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
 * 商户附件表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhMerchantFileServiceImpl implements PhMerchantFileService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhMerchantFileRepository phMerchantFileRepository;

    @Override
    public PhMerchantFile save(PhMerchantFile phMerchantFile) {
        return phMerchantFileRepository.save(phMerchantFile);
    }

    @Override
    public List<PhMerchantFile> findByPid(Long pid) {
        return phMerchantFileRepository.findAll(new Specification<PhMerchantFile>() {
            @Override
            public Predicate toPredicate(Root<PhMerchantFile> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("merchantId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public void del(Long id) {
        phMerchantFileRepository.delete(id);
    }

    @Override
    public void delByPid(Long pid) {
        phMerchantFileRepository.deleteByPid(pid);
    }

    @Override
    public PhMerchantFile findOne(Long id) {
        return phMerchantFileRepository.findOne(id);
    }
}
