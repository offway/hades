package cn.offway.hades.service;

import cn.offway.hades.domain.PhRefund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 退款/退货Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhRefundService {

    PhRefund save(PhRefund phRefund);

    PhRefund findOne(Long id);

    Page<PhRefund> list(Object mid, String orderNo, Date sTime, Date eTime, String userId, Date sTimeCheck, Date eTimeCheck, String[] type, String status, Pageable pageable);

    List<PhRefund> all(String orderNo, Date sTime, Date eTime, String userId, Date sTimeCheck, Date eTimeCheck, String[] type, String status);

    PhRefund findOne(String orderNo);

    PhRefund findOne(String orderNo, String... status);
}
