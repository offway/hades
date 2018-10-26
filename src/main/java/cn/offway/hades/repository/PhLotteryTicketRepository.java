package cn.offway.hades.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.hades.domain.PhLotteryTicket;


/**
 * 抽奖券表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhLotteryTicketRepository extends JpaRepository<PhLotteryTicket,Long>,JpaSpecificationExecutor<PhLotteryTicket> {

	@Modifying
    @Transactional
	@Query(nativeQuery=true,value="UPDATE ph_lottery_ticket set CODE=CONCAT('OW',LPAD(id, 6, 0)) where CODE is null")
	int updateCode();
	
	int countByProductIdAndUnionidAndSource(Long productId,String unionid,String source);
	
	List<PhLotteryTicket> findByProductIdAndUnionid(Long productId,String unionid);
}
