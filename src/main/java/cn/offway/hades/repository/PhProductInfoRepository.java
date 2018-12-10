package cn.offway.hades.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.hades.domain.PhProductInfo;

/**
 * 活动产品表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhProductInfoRepository extends JpaRepository<PhProductInfo,Long>,JpaSpecificationExecutor<PhProductInfo> {

	List<PhProductInfo> findByEndTime(Date endTime);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true,value="insert into sequence VALUES(?1,0,1)")
	int sequence(Long productId);
	
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true,value="update ph_product_info set sort = (select  t.sort+1 from (select MAX(sort)as sort from ph_product_info ) t ) where id=?1")
	int updateSort(Long productId);
	
}
