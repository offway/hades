package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhBrandRecommend;

/**
 * 品牌推荐表Repository接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
public interface PhBrandRecommendRepository extends JpaRepository<PhBrandRecommend,Long>,JpaSpecificationExecutor<PhBrandRecommend> {

	/** 此处写一些自定义的方法 **/
}
