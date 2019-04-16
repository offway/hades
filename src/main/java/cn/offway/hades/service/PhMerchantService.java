package cn.offway.hades.service;

import cn.offway.hades.domain.PhMerchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商户表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhMerchantService {

    PhMerchant save(PhMerchant phMerchant);

    PhMerchant findOne(Long id);

    PhMerchant findByAdminId(Long adminId);

    List<PhMerchant> findAll();

    List<PhMerchant> findAll(Long id);

    Page<PhMerchant> findAll(Pageable pageable);

    Page<PhMerchant> findAll(String name, Pageable pageable);

    void del(Long id);
}
