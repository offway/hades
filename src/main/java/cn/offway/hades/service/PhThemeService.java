package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhTheme;

/**
 * 主题列表Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
public interface PhThemeService{

    PhTheme save(PhTheme phTheme);
	
    PhTheme findOne(Long id);

    void delete(Long id);

    List<PhTheme> save(List<PhTheme> entities);
}
