package cn.offway.hades.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.hades.domain.PhActivityJoin;
import java.lang.Long;

/**
 * 活动参与表-每日福利Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhActivityJoinRepository extends JpaRepository<PhActivityJoin,Long>,JpaSpecificationExecutor<PhActivityJoin> {

	 int countByUnionidAndActivityId(String unionid,Long activityId);
	 
	 List<PhActivityJoin> findByActivityId(Long activityId);
	 
	 List<PhActivityJoin> findByActivityIdAndIsLucky(Long activityId,String isLucky);
	 
	 @Query(nativeQuery=true,value="select aj.* from ph_activity_join aj where aj.activity_id=?1 and  not EXISTS (select 1 from ph_activity_blacklist ab where ab.unionid = aj.unionid) and not exists (select 1 from ph_activity_prize ap where ap.unionid = aj.unionid) order by RAND() limit ?2")
	 List<PhActivityJoin> luckly(Long activityId,Long num);
	 
	 @Transactional
	 @Modifying
	 @Query(nativeQuery=true,value="update ph_activity_join set is_lucky='1' where id in(?1) ")
	 int updateLuckly(List<Long> ids);
	 
	 @Query(nativeQuery=true,value="select i.miniopenid,MIN(t.form_id) from ph_activity_join t,ph_wxuser_info i where  t.unionid = i.unionid and t.activity_id =?1 and form_id is not null and  form_id !='the formId is a mock one' group by i.miniopenid")
	 List<Object> findNoticeData(Long activityId);
	 
	 /**
	  * 查询参加本活动，中奖时间最早的用户
	  * @param activityId
	  * @return
	  */
	 @Query(nativeQuery = true,value="select aj.*  from ph_activity_join aj  where aj.activity_id=?1 and  not EXISTS (select 1 from ph_activity_blacklist ab where ab.unionid = aj.unionid) order by (select MAX(create_time) from ph_activity_prize  ap where aj.unionid = ap.unionid) ")
	 List<PhActivityJoin> findWinBefore(Long activityId);
	 
	 
}
