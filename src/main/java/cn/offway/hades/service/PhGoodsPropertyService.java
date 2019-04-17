package cn.offway.hades.service;

import cn.offway.hades.domain.PhGoodsProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品属性Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsPropertyService {

    PhGoodsProperty save(PhGoodsProperty phGoodsProperty);

    PhGoodsProperty findOne(Long id);

    Page<PhGoodsProperty> findAll(Pageable pageable);

    Page<PhGoodsProperty> findAll(String name, Pageable pageable);

    void del(Long id);

    void delByPid(Long id);

    List<PhGoodsProperty> findByPid(Long pid);
}
