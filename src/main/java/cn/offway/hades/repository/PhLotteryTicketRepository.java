package cn.offway.hades.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.hades.domain.PhLotteryTicket;
import cn.offway.hades.dto.VTicketCount;
import java.lang.String;


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
	
	@Query(nativeQuery=true,value="select i.miniopenid,MIN(t.form_id) from ph_lottery_ticket t,ph_wxuser_info i where  t.unionid = i.unionid and t.product_id =?1 and form_id is not null and  form_id !='the formId is a mock one' and source='0' group by i.miniopenid")
	List<Object> findNoticeData(Long productId);
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="update ph_lottery_ticket set head_url=?4,nick_name=?3 where product_id=?1 and unionid=?2")
	int editHead(Long productId,String unionid,String nickName,String headUrl);
	
	int countByProductIdAndCodeIn(Long productId,List<String> codes);
}
