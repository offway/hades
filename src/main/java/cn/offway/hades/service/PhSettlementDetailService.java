package cn.offway.hades.service;

import cn.offway.hades.domain.PhSettlementDetail;

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
}
