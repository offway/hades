package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhStarsameComments;
import cn.offway.hades.repository.PhActivityCommentsRepository;
import cn.offway.hades.service.PhStarsameCommentsService;
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
 * 文章评论Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-18 16:00:10 Exp $
 */
@Service
public class PhStarsameCommentsServiceImpl implements PhStarsameCommentsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhActivityCommentsRepository phActivityCommentsRepository;

    @Override
    public PhStarsameComments save(PhStarsameComments phActivityComments) {
        return phActivityCommentsRepository.save(phActivityComments);
    }

    @Override
    public PhStarsameComments findOne(Long id) {
        return phActivityCommentsRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phActivityCommentsRepository.delete(id);
    }

    @Override
    public Page<PhStarsameComments> findAll(String pid, String id, String userId, String content, Pageable pageable) {
        return phActivityCommentsRepository.findAll(new Specification<PhStarsameComments>() {
            @Override
            public Predicate toPredicate(Root<PhStarsameComments> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(cb.equal(root.get("articleId"), pid));
                if (!"".equals(id)) {
                    params.add(cb.equal(root.get("id"), id));
                }
                if (!"".equals(userId)) {
                    params.add(cb.equal(root.get("userId"), userId));
                }
                if (!"".equals(content)) {
                    params.add(cb.like(root.get("content"), "%" + content + "%"));
                }
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhStarsameComments> save(List<PhStarsameComments> entities) {
        return phActivityCommentsRepository.save(entities);
    }
}
