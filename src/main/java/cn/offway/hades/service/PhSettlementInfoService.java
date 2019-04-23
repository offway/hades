package cn.offway.hades.service;

import cn.offway.hades.domain.PhSettlementInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 商户结算表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhSettlementInfoService {

    PhSettlementInfo save(PhSettlementInfo phSettlementInfo);

    PhSettlementInfo findOne(Long id);

    PhSettlementInfo findByPid(Long merchantId);

    Boolean isExist(Long merchantId);

    Page<PhSettlementInfo> findAll(Pageable pageable);
}
