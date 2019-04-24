package cn.offway.hades.service;

import cn.offway.hades.domain.PhMerchantFare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商户运费模板Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhMerchantFareService {

    PhMerchantFare save(PhMerchantFare phMerchantFare);

    PhMerchantFare findOne(Long id);

    List<PhMerchantFare> getByPid(Long pid);

    Page<PhMerchantFare> getByPidPage(Long pid, Pageable pageable);

    Page<PhMerchantFare> getByPidPage(Long pid, String remark, Pageable pageable);

    void del(Long id);

    void makeAllToUnDefault(Long pid);

    Long getCountByPid(Long pid);
}
