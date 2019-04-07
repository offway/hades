package cn.offway.hades.service;

import cn.offway.hades.domain.PhBanner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhBannerService {
    PhBanner save(PhBanner banner);

    PhBanner findOne(long id);

    Page<PhBanner> findAll(Pageable pageable);

    void delete(long id);
}
