package cn.offway.hades.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.hades.domain.PhLotteryTicket;


/**
 * 抽奖券表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhLotteryTicketService{

	PhLotteryTicket save(PhLotteryTicket phLotteryTicket);
	
	PhLotteryTicket findOne(Long id);
	
	int countByProductIdAndUnionidAndSource(Long productId, String unionid, String source);

	List<PhLotteryTicket> findByProductIdAndUnionid(Long productId, String unionid);

	Page<PhLotteryTicket> findByPage(String code, Pageable page);

	void notice(Long productId);

}
