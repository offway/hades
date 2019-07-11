package cn.offway.hades.service;

import cn.offway.hades.domain.PhSettlementDetail;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 商户结算明细表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhSettlementDetailService {

    PhSettlementDetail save(PhSettlementDetail phSettlementDetail);

    PhSettlementDetail findOne(Long id);

    PhSettlementDetail findOne(String orderNo);

    Boolean isExist(String orderNo);

    Iterable<PhSettlementDetail> findAll(Long merchantId, Date sTime, Date eTime, String orderStatus, String status, String payChannel, Pageable pageable);

    List<PhSettlementDetail> findList(Long[] idList);
}
