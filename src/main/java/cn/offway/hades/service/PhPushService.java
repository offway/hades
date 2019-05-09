package cn.offway.hades.service;

import cn.offway.hades.domain.PhPush;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * 推送记录Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhPushService {

    PhPush save(PhPush phPush);

    PhPush findOne(Long id);

    Page<PhPush> list(String name, String content, String type, Date sTime, Date eTime, Pageable pageable);

    void del(Long id);
}
