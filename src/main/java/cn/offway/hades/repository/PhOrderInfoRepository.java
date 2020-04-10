package cn.offway.hades.repository;

import cn.offway.hades.domain.PhOrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 订单Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhOrderInfoRepository extends JpaRepository<PhOrderInfo, Long>, JpaSpecificationExecutor<PhOrderInfo> {
    @Query(nativeQuery = true, value = "SELECT sum(amount) as amount,user_id,count(*) as ct FROM ph_order_info where status in (1,2,3) group by user_id order by sum(amount) desc limit 100")
    List<Object[]> stat();

    @Query(nativeQuery = true, value = "SELECT sum(amount) as amount,user_id,count(*) as ct FROM ph_order_info where status in (1,2,3) and create_time > ?1 group by user_id order by sum(amount) desc limit 100")
    List<Object[]> statGt(String sTime);

    @Query(nativeQuery = true, value = "SELECT sum(amount) as amount,user_id,count(*) as ct FROM ph_order_info where status in (1,2,3) and create_time < ?1 group by user_id order by sum(amount) desc limit 100")
    List<Object[]> statLt(String eTime);

    @Query(nativeQuery = true, value = "SELECT sum(amount) as amount,user_id,count(*) as ct FROM ph_order_info where status in (1,2,3) and create_time between ?1 and ?2 group by user_id order by sum(amount) desc limit 100")
    List<Object[]> stat(String sTime, String eTime);
}
