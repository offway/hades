package cn.offway.hades.service;

import java.util.List;

import cn.offway.hades.domain.PhActivityInfo;
import cn.offway.hades.domain.PhActivityJoin;
import cn.offway.hades.domain.PhWxuserInfo;

/**
 * 活动参与表-每日福利Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhActivityJoinService{

	PhActivityJoin save(PhActivityJoin phActivityJoin);
	
	PhActivityJoin findOne(Long id);

	int countByUnionidAndActivityId(String unionid, Long activityId);

	void join(PhActivityInfo phActivityInfo, PhWxuserInfo phWxuserInfo);

	List<PhActivityJoin> findByActivityId(Long activityId);

	List<PhActivityJoin> luckly(Long activityId, Long num);

	int updateLuckly(List<Long> ids);

	List<Object> findNoticeData(Long activityId);
}
