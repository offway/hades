package cn.offway.hades.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.offway.hades.domain.PhActivityImage;

/**
 * 活动图片表-每日福利Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhActivityImageRepository extends JpaRepository<PhActivityImage,Long>,JpaSpecificationExecutor<PhActivityImage> {

	@Query(nativeQuery=true,value="select * from ph_activity_image where activity_id=?1 order by sort")
	List<PhActivityImage> findByActivityId(Long activityId);
}
