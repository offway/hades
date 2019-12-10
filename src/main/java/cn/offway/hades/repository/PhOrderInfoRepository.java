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
}
