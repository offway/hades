package cn.offway.hades.service;

import cn.offway.hades.domain.PhGoodsImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 商品图片Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhGoodsImageService {

    PhGoodsImage save(PhGoodsImage phGoodsImage);

    PhGoodsImage findOne(Long id);

    Page<PhGoodsImage> findAll(Pageable pageable);

    Page<PhGoodsImage> findAll(String name, Pageable pageable);

    void del(Long id);

    void delByPid(Long id);
}
