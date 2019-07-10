package cn.offway.hades.service;

import cn.offway.hades.domain.PhBanner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhBannerService {
    PhBanner save(PhBanner phBanner);

    PhBanner findOne(Long id);

    Page<PhBanner> findAll(String position, Pageable pageable);

    void delete(Long id);

    void resort(String position, Long sort);

    Long getMaxSort(String position);

    void updatetime();

    Page<PhBanner> findAll(String position, String id, String remark, Pageable pageable);
}
