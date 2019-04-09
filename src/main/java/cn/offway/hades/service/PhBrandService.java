package cn.offway.hades.service;

import cn.offway.hades.domain.PhBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 品牌库Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhBrandService {

    PhBrand save(PhBrand phBrand);

    PhBrand findOne(Long id);

    Page<PhBrand> findAll(Pageable pageable);

    void del(Long id);
}
