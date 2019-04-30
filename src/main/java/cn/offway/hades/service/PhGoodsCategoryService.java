package cn.offway.hades.service;

import cn.offway.hades.domain.PhGoodsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品类目Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsCategoryService {

    PhGoodsCategory save(PhGoodsCategory phGoodsCategory);

    PhGoodsCategory findOne(Long id);

    Page<PhGoodsCategory> findAll(Pageable pageable);

    List<PhGoodsCategory> findAll();

    void del(Long id);

    List<PhGoodsCategory> findByPid(Long pid);

    Page<PhGoodsCategory> findByPid(Long pid, Pageable pageable);

    void delByPid(Long pid);
}
