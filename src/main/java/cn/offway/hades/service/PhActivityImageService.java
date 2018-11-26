package cn.offway.hades.service;

import java.util.List;

import cn.offway.hades.domain.PhActivityImage;

/**
 * 活动图片表-每日福利Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhActivityImageService{

	PhActivityImage save(PhActivityImage phActivityImage);
	
	PhActivityImage findOne(Long id);

	List<PhActivityImage> findByActivityId(Long activityId);
}
