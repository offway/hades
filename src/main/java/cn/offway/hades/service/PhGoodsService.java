package cn.offway.hades.service;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.domain.PhMerchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsService {

    PhGoods save(PhGoods phGoods);

    PhGoods findOne(Long id);

    Page<PhGoods> findAll(Pageable pageable);

    Page<PhGoods> findAll(String name, String code, Pageable pageable);

    Page<PhGoods> findAll(String name, Long id, String code, String status, Long merchantId, Long merchantBrandId, String type, String category, Long[] gidArr, boolean inOrNot, boolean isLimit, Pageable pageable);

    void del(Long id);

    Long getCountByPid(Long merchantId);

    List<PhGoods> findAll(String name, Object value);

    List<PhGoods> findByIds(Long[] ids);

    void updateMerchantInfo(PhMerchant merchant);

    void updateBrandInfo(PhBrand brand);

    List<PhGoods> findAllAlt(String mid, String bid);
}
