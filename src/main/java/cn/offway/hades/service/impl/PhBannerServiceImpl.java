package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhBanner;
import cn.offway.hades.repository.PhBannerRepository;
import cn.offway.hades.service.PhBannerService;
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

/*
Banner管理Service接口实现
 */
@Service
public class PhBannerServiceImpl implements PhBannerService {

    @Autowired
    private PhBannerRepository bannerRepository;

    @Override
    public PhBanner save(PhBanner banner) {
        return bannerRepository.save(banner);
    }

    @Override
    public PhBanner findOne(Long id) {
        return bannerRepository.findOne(id);
    }

    @Override
    public Page<PhBanner> findAll(String position, Pageable pageable) {
        return bannerRepository.findAll(new Specification<PhBanner>() {
            @Override
            public Predicate toPredicate(Root<PhBanner> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(criteriaBuilder.equal(root.get("position"), position));
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public Long getMaxSort(String position) {
        Optional<String> res = bannerRepository.getMaxSort(position);
        if (res.isPresent()) {
            return Long.valueOf(String.valueOf(res.get()));
        } else {
            return 0L;
        }
    }

    @Override
    public void resort(String position, Long sort) {
        bannerRepository.resort(position, sort);
    }

    @Override
    public void delete(Long id) {
        bannerRepository.delete(id);
    }
}
