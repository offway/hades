package cn.offway.hades.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.hades.domain.PhActivityInfo;

/**
 * 活动表-每日福利Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhActivityInfoService{

	PhActivityInfo save(PhActivityInfo phActivityInfo);
	
	PhActivityInfo findOne(Long id);

	Map<String, List<PhActivityInfo>> list();

	Map<String, Object> detail(Long activityId, String unionid);

	List<PhActivityInfo> findByEndTime(Date endtime);

	Page<PhActivityInfo> findByPage(String name, Pageable page);

	void save(PhActivityInfo phActivityInfo, String banner, String detail);

	boolean imagesDelete(Long activityImageId);

	List<PhActivityInfo> findAll(List<Long> ids);

	List<PhActivityInfo> save(List<PhActivityInfo> phActivityInfo);
}
