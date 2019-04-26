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
    public Page<PhBanner> findAll(Pageable pageable) {
        return bannerRepository.findAll(new Specification<PhBanner>() {
            @Override
            public Predicate toPredicate(Root<PhBanner> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        }, pageable);
    }

    @Override
    public Long getMaxSort() {
        Optional<String> res = bannerRepository.getMaxSort();
        if (res.isPresent()) {
            return Long.valueOf(String.valueOf(res.get()));
        } else {
            return 0L;
        }
    }

    @Override
    public void resort(Long sort) {
        bannerRepository.resort(sort);
    }

    @Override
    public void delete(Long id) {
        bannerRepository.delete(id);
    }
}
