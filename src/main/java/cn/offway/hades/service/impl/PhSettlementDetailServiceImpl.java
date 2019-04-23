package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhSettlementDetail;
import cn.offway.hades.repository.PhSettlementDetailRepository;
import cn.offway.hades.service.PhSettlementDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * 商户结算明细表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhSettlementDetailServiceImpl implements PhSettlementDetailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhSettlementDetailRepository phSettlementDetailRepository;

    @Override
    public PhSettlementDetail save(PhSettlementDetail phSettlementDetail) {
        return phSettlementDetailRepository.save(phSettlementDetail);
    }

    @Override
    public PhSettlementDetail findOne(String orderNo) {
        return null;
    }

    @Override
    public Boolean isExist(String orderNo) {
        Optional<String> res = phSettlementDetailRepository.getCount(orderNo);
        if (res.isPresent()) {
            return Integer.valueOf(String.valueOf(res.get())) > 0;
        } else {
            return false;
        }
    }

    @Override
    public PhSettlementDetail findOne(Long id) {
        return phSettlementDetailRepository.findOne(id);
    }
}
