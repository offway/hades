package cn.offway.hades.service;

import cn.offway.hades.domain.PhVoucherInfo;

import java.util.List;

/**
 * 优惠券Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhVoucherInfoService {

    PhVoucherInfo save(PhVoucherInfo phVoucherInfo);

    PhVoucherInfo findOne(Long id);

    void delByPid(Long pid);

    List<PhVoucherInfo> getByPid(Long pid);
}
