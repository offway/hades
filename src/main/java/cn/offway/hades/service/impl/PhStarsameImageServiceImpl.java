package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhStarsameImage;
import cn.offway.hades.repository.PhStarsameImageRepository;
import cn.offway.hades.service.PhStarsameImageService;
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
 * 明星同款banner图片Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhStarsameImageServiceImpl implements PhStarsameImageService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhStarsameImageRepository phStarsameImageRepository;

    @Override
    public PhStarsameImage save(PhStarsameImage phStarsameImage) {
        return phStarsameImageRepository.save(phStarsameImage);
    }

    @Override
    public void deleteByPid(Long pid) {
        phStarsameImageRepository.deleteByPid(pid);
    }

    @Override
    public List<PhStarsameImage> findAllByPid(Long pid) {
        return phStarsameImageRepository.findAll(new Specification<PhStarsameImage>() {
            @Override
            public Predicate toPredicate(Root<PhStarsameImage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("starsameId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public PhStarsameImage findOne(Long id) {
        return phStarsameImageRepository.findOne(id);
    }
}
