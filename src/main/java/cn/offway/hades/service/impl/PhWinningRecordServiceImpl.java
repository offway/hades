package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhWinningRecord;
import cn.offway.hades.repository.PhWinningRecordRepository;
import cn.offway.hades.service.PhWinningRecordService;
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
 * 中奖用户信息Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhWinningRecordServiceImpl implements PhWinningRecordService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhWinningRecordRepository phWinningRecordRepository;

    @Override
    public PhWinningRecord save(PhWinningRecord phWinningRecord) {
        return phWinningRecordRepository.save(phWinningRecord);
    }

    @Override
    public List<PhWinningRecord> findByPid(Long pid) {
        return phWinningRecordRepository.findAll(new Specification<PhWinningRecord>() {
            @Override
            public Predicate toPredicate(Root<PhWinningRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(cb.equal(root.get("productId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public PhWinningRecord findOne(Long id) {
        return phWinningRecordRepository.findOne(id);
    }

    @Override
    public int saveWin(Long productId, List<String> codes) {
        return phWinningRecordRepository.saveWin(productId, codes);
    }
}
