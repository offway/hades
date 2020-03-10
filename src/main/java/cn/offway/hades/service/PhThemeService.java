package cn.offway.hades.service;


import cn.offway.hades.domain.PhTheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 主题列表Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
public interface PhThemeService {

    PhTheme save(PhTheme phTheme);

    PhTheme findOne(Long id);

    void delete(Long id);

    List<PhTheme> save(List<PhTheme> entities);

    Page<PhTheme> list(Long id, String name, Date sTime, Date eTime, Pageable pageable);
}
