package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhTheme;
import cn.offway.hades.repository.PhThemeRepository;
import cn.offway.hades.service.PhThemeService;
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
 * 主题列表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
@Service
public class PhThemeServiceImpl implements PhThemeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhThemeRepository phThemeRepository;

    @Override
    public PhTheme save(PhTheme phTheme) {
        return phThemeRepository.save(phTheme);
    }

    @Override
    public PhTheme findOne(Long id) {
        return phThemeRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phThemeRepository.delete(id);
    }

    @Override
    public Page<PhTheme> list(Long id, String name, Date sTime, Date eTime, Pageable pageable) {
        return phThemeRepository.findAll(new Specification<PhTheme>() {
            @Override
            public Predicate toPredicate(Root<PhTheme> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (id != 0L) {
                    params.add(cb.equal(root.get("id"), id));
                }
                if (!"".equals(name)) {
                    params.add(cb.like(root.get("name"), "%" + name + "%"));
                }
                if (sTime != null && eTime != null) {
                    params.add(cb.between(root.get("createTime"), sTime, eTime));
                }
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhTheme> save(List<PhTheme> entities) {
        return phThemeRepository.save(entities);
    }
}
