package cn.offway.hades.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.hades.domain.PhLotteryTicket;
import cn.offway.hades.domain.PhProductInfo;
import cn.offway.hades.dto.VTicketCount;


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

	void notice() throws Exception;

	void notice(String token, PhProductInfo phProductInfo) throws Exception;

	String getToken();

	boolean ticketSave(Long productId, String unionid, String nickName, String headUrl);

	Page<VTicketCount> findVTicketCount(Long productId, String nickName, String unionid, int index, int pageSize);

	Page<PhLotteryTicket> findByPage(String code, Long productId, String nickName, String unionid, Pageable page);

	boolean checkCodes(Long productId, List<String> codes);

}
