package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhMerchantFareSpecial;

/**
 * 商户运费特殊表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhMerchantFareSpecialRepository extends JpaRepository<PhMerchantFareSpecial,Long>,JpaSpecificationExecutor<PhMerchantFareSpecial> {

	/** 此处写一些自定义的方法 **/
}
