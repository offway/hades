package cn.offway.hades.repository;

import cn.offway.hades.domain.PhBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PhBannerRepository extends JpaRepository<PhBanner, Long>, JpaSpecificationExecutor<PhBanner> {
    //
}
