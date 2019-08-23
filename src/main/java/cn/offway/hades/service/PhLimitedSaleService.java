package cn.offway.hades.service;

import cn.offway.hades.domain.PhLimitedSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 限量发售Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhLimitedSaleService {

    PhLimitedSale save(PhLimitedSale phLimitedSale);

    PhLimitedSale findOne(Long id);

    Page<PhLimitedSale> list(Pageable pageable);

    void del(Long id);

    void issetshow(Long id);

    void isshow();
}
