package cn.offway.hades.service;

import cn.offway.hades.domain.PhPromotionInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 促销活动Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhPromotionInfoService{

	PhPromotionInfo save(PhPromotionInfo phPromotionInfo);
	
	PhPromotionInfo findOne(Long id);

	Page<PhPromotionInfo> findAll(String type,String status,String mode,Pageable pageable);

	void del(Long id);
}
