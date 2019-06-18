package cn.offway.hades.service;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.domain.PhGoodsStock;
import cn.offway.hades.domain.PhMerchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    Page<PhGoodsStock> findAll(String name, String remark, Pageable pageable);

    void del(Long id);

    void delByPid(Long id);

    void updateByPid(Long id, Double value);

    List<PhGoodsStock> findByPid(Long pid);

    List<PhGoodsStock> findByPids(Long[] ids);

    void updateMerchantInfo(PhMerchant merchant);

    void updateBrandInfo(PhBrand brand);
}
