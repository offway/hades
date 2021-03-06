package cn.offway.hades.service;


import cn.offway.hades.domain.PhWithdrawInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 提现订单Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-07-25 13:22:40 Exp $
 */
public interface PhWithdrawInfoService {

    PhWithdrawInfo save(PhWithdrawInfo phWithdrawInfo);

    PhWithdrawInfo findOne(Long id);

    void delete(Long id);

    List<PhWithdrawInfo> save(List<PhWithdrawInfo> entities);

    Page<PhWithdrawInfo> findAll(Pageable pageable);

    Page<PhWithdrawInfo> findAll(String userId, Double miniamount, Double maxamount, String status, Date startTime, Date endTime, Pageable pageable);
}
