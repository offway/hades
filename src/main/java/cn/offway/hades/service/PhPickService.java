package cn.offway.hades.service;

import cn.offway.hades.domain.PhPick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * OFFWAY优选Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhPickService {

    PhPick save(PhPick phPick);

    PhPick findOne(Long id);

    Page<PhPick> list(Long id, String name, Date sTime, Date eTime, Pageable pageable);

    void del(Long id);
}
