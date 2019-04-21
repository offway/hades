package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhVoucherInfo;
import cn.offway.hades.repository.PhVoucherInfoRepository;
import cn.offway.hades.service.PhVoucherInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 优惠券Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhVoucherInfoServiceImpl implements PhVoucherInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhVoucherInfoRepository phVoucherInfoRepository;

    @Override
    public PhVoucherInfo save(PhVoucherInfo phVoucherInfo) {
        return phVoucherInfoRepository.save(phVoucherInfo);
    }

    @Override
    public void delByPid(Long pid) {
        phVoucherInfoRepository.deleteByPid(pid);
    }

    @Override
    public PhVoucherInfo findOne(Long id) {
        return phVoucherInfoRepository.findOne(id);
    }
}
