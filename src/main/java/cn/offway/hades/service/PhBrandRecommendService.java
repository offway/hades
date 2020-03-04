package cn.offway.hades.service;


import cn.offway.hades.domain.PhBrandRecommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 品牌推荐表Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
public interface PhBrandRecommendService {

    PhBrandRecommend save(PhBrandRecommend phBrandRecommend);

    PhBrandRecommend findOne(Long id);

    void delete(Long id);

    List<PhBrandRecommend> save(List<PhBrandRecommend> entities);

    Page<PhBrandRecommend> list(Pageable pageable);
}
