package cn.offway.hades.service;

import cn.offway.hades.domain.PhGoodsStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 商品库存Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsStockService {

    PhGoodsStock save(PhGoodsStock phGoodsStock);

    PhGoodsStock findOne(Long id);

    Page<PhGoodsStock> findAll(Pageable pageable);

    Page<PhGoodsStock> findAll(String name, Pageable pageable);

    void del(Long id);

    void delByPid(Long id);
}
