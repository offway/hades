package cn.offway.hades.service;

import cn.offway.hades.domain.PhOrderInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 订单Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhOrderInfoService {

    PhOrderInfo save(PhOrderInfo phOrderInfo);

    PhOrderInfo findOne(Long id);

    PhOrderInfo findOne(String orderNo);

    Iterable<PhOrderInfo> findAll(Long mid, String orderNo, Date sTime, Date eTime, String userId, String payMethod, String[] status, Pageable pageable);

    List<PhOrderInfo> findAll(Long mid, String orderNo, Date sTime, Date eTime, String userId, String payMethod, String[] status);

    Page<PhOrderInfo> findAll(String pid, Pageable pageable);

    List<PhOrderInfo> findToCheck(Date start, Date stop);

    List<PhOrderInfo> findToProcess(Date start, Date stop);

    List<PhOrderInfo> findAll(String preOrderNo);

    List<PhOrderInfo> findByPreorderNoAndStatus(String preorderno, String... status);

    List<Object[]> stat();
}
