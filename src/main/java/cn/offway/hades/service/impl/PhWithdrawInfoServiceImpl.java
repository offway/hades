package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhWithdrawInfo;
import cn.offway.hades.repository.PhWithdrawInfoRepository;
import cn.offway.hades.service.PhWithdrawInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 提现订单Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-07-25 13:22:40 Exp $
 */
@Service
public class PhWithdrawInfoServiceImpl implements PhWithdrawInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhWithdrawInfoRepository phWithdrawInfoRepository;

    @Override
    public PhWithdrawInfo save(PhWithdrawInfo phWithdrawInfo) {
        return phWithdrawInfoRepository.save(phWithdrawInfo);
    }

    @Override
    public PhWithdrawInfo findOne(Long id) {
        return phWithdrawInfoRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phWithdrawInfoRepository.delete(id);
    }

    @Override
    public List<PhWithdrawInfo> save(List<PhWithdrawInfo> entities) {
        return phWithdrawInfoRepository.save(entities);
    }

    @Override
    public Page<PhWithdrawInfo> findAll(Pageable pageable) {
        return phWithdrawInfoRepository.findAll(pageable);
    }
}
