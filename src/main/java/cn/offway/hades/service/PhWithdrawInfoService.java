package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhWithdrawInfo;

/**
 * 提现订单Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-07-25 13:22:40 Exp $
 */
public interface PhWithdrawInfoService{

    PhWithdrawInfo save(PhWithdrawInfo phWithdrawInfo);
	
    PhWithdrawInfo findOne(Long id);

    void delete(Long id);

    List<PhWithdrawInfo> save(List<PhWithdrawInfo> entities);
}
