package cn.offway.hades.service;

import cn.offway.hades.domain.PhPreorderInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;


/**
 * 预生成订单Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhPreorderInfoService {

    PhPreorderInfo save(PhPreorderInfo phPreorderInfo);

    PhPreorderInfo findOne(Long id);

    PhPreorderInfo findByOrderNoAndStatus(String orderno, String status);

    int countByUserIdAndStatus(Long userId, String status);

    Page<PhPreorderInfo> findAll(String orderNo, Date sTime, Date eTime, String userId, String payMethod, String status, Pageable pageable);
}
