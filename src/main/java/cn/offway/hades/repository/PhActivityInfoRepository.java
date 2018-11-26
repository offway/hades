package cn.offway.hades.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.offway.hades.domain.PhActivityInfo;
import java.util.Date;

/**
 * 活动表-每日福利Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhActivityInfoRepository extends JpaRepository<PhActivityInfo,Long>,JpaSpecificationExecutor<PhActivityInfo> {

	@Query(nativeQuery=true,value="select * from ph_activity_info where status ='1' and NOW() >= begin_time and NOW() < end_time order by begin_time desc")
	List<PhActivityInfo> findByNow();
	
	@Query(nativeQuery=true,value="select * from ph_activity_info where status ='1' and  NOW() >= end_time order by end_time desc limit 15")
	List<PhActivityInfo> findByBefore();
	
	List<PhActivityInfo> findByEndTime(Date endtime);
}
