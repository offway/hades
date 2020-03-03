package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhTheme;

/**
 * 主题列表Repository接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
public interface PhThemeRepository extends JpaRepository<PhTheme,Long>,JpaSpecificationExecutor<PhTheme> {

	/** 此处写一些自定义的方法 **/
}
