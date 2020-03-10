package cn.offway.hades.service;


import cn.offway.hades.domain.PhThemeGoods;

import java.util.List;

/**
 * 主题商品表Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
public interface PhThemeGoodsService {

    PhThemeGoods save(PhThemeGoods phThemeGoods);

    PhThemeGoods findOne(Long id);

    void delete(Long id);

    List<PhThemeGoods> save(List<PhThemeGoods> entities);

    List<PhThemeGoods> findByPid(Long pid);

    void delByPid(Long pid);
}
