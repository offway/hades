package cn.offway.hades.service;

import cn.offway.hades.domain.PhVoucherProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * 优惠券方案Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhVoucherProjectService {

    PhVoucherProject save(PhVoucherProject phVoucherProject);

    PhVoucherProject findOne(Long id);

    Page<PhVoucherProject> list(String type, String name, Long merchantId, Date beginTime, Date endTime, String remark, String isPrivate, Pageable pageable);

    void del(Long id);
}
