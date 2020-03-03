package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhThemeGoods;

/**
 * 主题商品表Repository接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
public interface PhThemeGoodsRepository extends JpaRepository<PhThemeGoods,Long>,JpaSpecificationExecutor<PhThemeGoods> {

	/** 此处写一些自定义的方法 **/
}
