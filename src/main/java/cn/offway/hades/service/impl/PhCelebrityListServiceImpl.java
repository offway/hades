package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhCelebrityList;
import cn.offway.hades.repository.PhCelebrityListRepository;
import cn.offway.hades.service.PhCelebrityListService;
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
import java.util.List;


/**
 * 明星信息表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
@Service
public class PhCelebrityListServiceImpl implements PhCelebrityListService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhCelebrityListRepository phCelebrityListRepository;

    @Override
    public PhCelebrityList save(PhCelebrityList phCelebrityList) {
        return phCelebrityListRepository.save(phCelebrityList);
    }

    @Override
    public PhCelebrityList findOne(Long id) {
        return phCelebrityListRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phCelebrityListRepository.delete(id);
    }

    @Override
    public Page<PhCelebrityList> list(String name, Pageable pageable) {
        return phCelebrityListRepository.findAll(new Specification<PhCelebrityList>() {
            @Override
            public Predicate toPredicate(Root<PhCelebrityList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (StringUtils.isNoneBlank(name)) {
                    params.add(cb.like(root.get("name"), "%" + name + "%"));
                }
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<Object[]> list(String name, int offset) {
        if (StringUtils.isBlank(name)) {
            return phCelebrityListRepository.list(offset);
        } else {
            return phCelebrityListRepository.list("%" + name + "%", offset);
        }
    }

    @Override
    public long count(String name) {
        if (StringUtils.isBlank(name)) {
            return phCelebrityListRepository.count();
        } else {
            return phCelebrityListRepository.count(new Specification<PhCelebrityList>() {
                @Override
                public Predicate toPredicate(Root<PhCelebrityList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> params = new ArrayList<Predicate>();
                    params.add(cb.like(root.get("name"), "%" + name + "%"));
                    Predicate[] predicates = new Predicate[params.size()];
                    query.where(params.toArray(predicates));
                    return null;
                }
            });
        }
    }

    @Override
    public List<PhCelebrityList> save(List<PhCelebrityList> entities) {
        return phCelebrityListRepository.save(entities);
    }
}
