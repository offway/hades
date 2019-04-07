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

@Service
public class PhBannerServiceImpl implements PhBannerService {

    @Autowired
    private PhBannerRepository bannerRepository;

    @Override
    public PhBanner save(PhBanner banner) {
        return bannerRepository.save(banner);
    }

    @Override
    public PhBanner findOne(long id) {
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
    public void delete(long id) {
        bannerRepository.delete(id);
    }
}
