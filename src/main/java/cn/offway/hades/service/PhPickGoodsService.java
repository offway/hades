package cn.offway.hades.service;

import cn.offway.hades.domain.PhPickGoods;

import java.util.List;

/**
 * OFFWAY优选商品明细Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhPickGoodsService {

    PhPickGoods save(PhPickGoods phPickGoods);

    PhPickGoods findOne(Long id);

    List<PhPickGoods> findByPid(Long pid);

    void delByPid(Long pid);
}
