package cn.offway.hades.service;

import cn.offway.hades.domain.PhGoods;
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

    Page<PhGoods> findAll(String name, Long id, String code, String status, Long merchantId, String type, String category, Long[] gidArr, boolean inOrNot, Pageable pageable);

    void del(Long id);

    Long getCountByPid(Long merchantId);

    List<PhGoods> findAll(Long pid);
}
