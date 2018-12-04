package cn.offway.hades.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.hades.domain.PhActivityPrize;

/**
 * 活动奖品表-每日福利Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhActivityPrizeService{

	PhActivityPrize save(PhActivityPrize phActivityPrize);
	
	PhActivityPrize findOne(Long id);

	PhActivityPrize findByActivityIdAndUnionid(Long activityid, String unionid);

	/**
	 * 开奖
	 * @param activityId
	 */
	void open() throws Exception;

	Page<PhActivityPrize> findByPage(String activityName, String nickName, String unionid, Long activityId,
			Pageable page);
}
