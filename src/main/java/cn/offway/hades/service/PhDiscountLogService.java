package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhDiscountLog;

/**
 * 定时任务记录表Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2019-12-25 14:31:38 Exp $
 */
public interface PhDiscountLogService{

    PhDiscountLog save(PhDiscountLog phDiscountLog);
	
    PhDiscountLog findOne(Long id);

    void delete(Long id);

    List<PhDiscountLog> save(List<PhDiscountLog> entities);
}
