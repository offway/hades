package cn.offway.hades.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.hades.domain.PhWinningRecord;

/**
 * 中奖用户信息Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhWinningRecordRepository extends JpaRepository<PhWinningRecord,Long>,JpaSpecificationExecutor<PhWinningRecord> {

	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="insert into ph_winning_record select null,product_id,unionid,head_url,nick_name,`code`,NOW(),NULL from ph_lottery_ticket where product_id=?1 and code in (?2)")
	int saveWin(Long productId,List<String> codes);
}
