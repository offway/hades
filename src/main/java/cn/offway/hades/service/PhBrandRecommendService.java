package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhBrandRecommend;

/**
 * 品牌推荐表Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
public interface PhBrandRecommendService{

    PhBrandRecommend save(PhBrandRecommend phBrandRecommend);
	
    PhBrandRecommend findOne(Long id);

    void delete(Long id);

    List<PhBrandRecommend> save(List<PhBrandRecommend> entities);
}
