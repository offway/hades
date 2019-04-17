package cn.offway.hades.service;

import cn.offway.hades.domain.PhMerchantFareSpecial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商户运费特殊表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhMerchantFareSpecialService {

    PhMerchantFareSpecial save(PhMerchantFareSpecial phMerchantFareSpecial);

    PhMerchantFareSpecial findOne(Long id);

    List<PhMerchantFareSpecial> getByPid(Long pid);

    Page<PhMerchantFareSpecial> getByPidPage(Long pid, Pageable pageable);

    Page<PhMerchantFareSpecial> getByPidPage(Long pid, String remark, Pageable pageable);

    void del(Long id);

    void delByPid(Long pid);
}
