package cn.offway.hades.service;

import cn.offway.hades.domain.PhUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * 用户信息Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhUserInfoService {

    PhUserInfo save(PhUserInfo phUserInfo);

    PhUserInfo findOne(Long id);

    Page<PhUserInfo> list(String phone, String nickname, String userId, String sex, Date sTime, Date eTime, String channel, Date sTimeReg, Date eTimeReg, Pageable pageable);

    int addPoints(Long id, Long points);

    Page<PhUserInfo> findAllByPage(String channel, Pageable page);
}
