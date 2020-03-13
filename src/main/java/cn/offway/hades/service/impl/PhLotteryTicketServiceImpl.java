package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhLotteryTicket;
import cn.offway.hades.domain.PhProductInfo;
import cn.offway.hades.dto.VTicketCount;
import cn.offway.hades.repository.PhLotteryTicketRepository;
import cn.offway.hades.service.PhLotteryTicketService;
import cn.offway.hades.service.PhProductInfoService;
import cn.offway.hades.utils.WXUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;




/**
 * 抽奖券表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhLotteryTicketServiceImpl implements PhLotteryTicketService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhLotteryTicketRepository phLotteryTicketRepository;
	
	@Autowired
	private PhProductInfoService phProductInfoService;
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public PhLotteryTicket save(PhLotteryTicket phLotteryTicket){
		return phLotteryTicketRepository.save(phLotteryTicket);
	}
	
	@Override
	public PhLotteryTicket findOne(Long id){
		return phLotteryTicketRepository.findOne(id);
	}
	
	@Override
	public int countByProductIdAndUnionidAndSource(Long productId,String unionid,String source){
		return phLotteryTicketRepository.countByProductIdAndUnionidAndSource(productId, unionid, source);
	}
	
	@Override
	public List<PhLotteryTicket> findByProductIdAndUnionid(Long productId,String unionid){
		return phLotteryTicketRepository.findByProductIdAndUnionid(productId,unionid);
	}
	
	@Override
	public boolean checkCodes(Long productId,List<String> codes){
		int count = phLotteryTicketRepository.countByProductIdAndCodeIn(productId, codes);
		if(count == codes.size()){
			return true;
		}
		return false;
	}
	
	@Override
	public Page<PhLotteryTicket> findByPage(final String code,final Long productId,final String nickName,final String unionid, Pageable page){
		return phLotteryTicketRepository.findAll(new Specification<PhLotteryTicket>() {
			
			@Override
			public Predicate toPredicate(Root<PhLotteryTicket> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(code)){
					params.add(criteriaBuilder.like(root.get("code"), "%"+code+"%"));
				}
				
				if(null != productId){
					params.add(criteriaBuilder.equal(root.get("productId"), productId));
				}
				if(StringUtils.isNotBlank(nickName)){
					params.add(criteriaBuilder.like(root.get("nickName"), "%"+nickName+"%"));
				}
				
				if(StringUtils.isNotBlank(unionid)){
					params.add(criteriaBuilder.equal(root.get("unionid"), unionid));
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		}, page);
	}
	
	
	@Override
	public Page<VTicketCount> findVTicketCount(Long productId,String nickName,String unionid,String isPersonnel,int index, int pageSize) {
		// 新建一个页面，存放页面信息
		Pageable page = new PageRequest(index, pageSize);
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<VTicketCount> criteriaQuery = criteriaBuilder.createQuery(VTicketCount.class);
		Root<PhLotteryTicket> root = criteriaQuery.from(PhLotteryTicket.class);
		criteriaQuery.multiselect(root.get("productId"),criteriaBuilder.min(root.get("nickName")),root.get("unionid"),criteriaBuilder.min(root.get("headUrl")), criteriaBuilder.count(root.get("code")));
		
		List<Predicate> params = new ArrayList<Predicate>();
		if(null != productId){
			params.add(criteriaBuilder.equal(root.get("productId"), productId));
		}
		if(StringUtils.isNotBlank(nickName)){
			params.add(criteriaBuilder.like(root.get("nickName"), "%"+nickName+"%"));
		}
		
		if(StringUtils.isNotBlank(isPersonnel)){
			
			In<String> in = criteriaBuilder.in(root.get("unionid"));
			String[] personnels = {
					"o9I8Z0nQyLMVjocrJMjZWAcCuhyA",
					"o9I8Z0vf3u6PzUzTrrE1cIm3a0Zs",
					"o9I8Z0n3_33kX2uOrm5N3_C1rkPU",
					"o9I8Z0kCzZNHoANAuNgUwHWjKwDM",
					"o9I8Z0phVei2hUpyRp5gmp9z12ag",
					"o9I8Z0n5tu6ivLHr_6gkSdNf8aZU",
					"o9I8Z0qLeYEGRVK-1A0ESvtSk2go",
					"o9I8Z0ioHMeRuYyb8k7sIB9SHndo",
					"o9I8Z0tLd1ZtvYujM6wL2irNAxVw",
					"o9I8Z0q8zPh0U2SJjezqrYPPYmxs"
			};
			for (String at : personnels) {
				in.value(at);
			}
			
			if("1".equals(isPersonnel)){
				params.add(in);
			}else if("0".equals(isPersonnel)){
				params.add(criteriaBuilder.not(in));
			}
		}
		
		if(StringUtils.isNotBlank(unionid)){
			params.add(criteriaBuilder.equal(root.get("unionid"), unionid));
		}
        Predicate[] predicates = new Predicate[params.size()];
        criteriaQuery.where(params.toArray(predicates));
		criteriaQuery.groupBy(root.get("productId"),root.get("unionid"));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("productId")),criteriaBuilder.desc(criteriaBuilder.count(root.get("code"))));
		
		TypedQuery<VTicketCount> createQuery = entityManager.createQuery(criteriaQuery);
		createQuery.setFirstResult(index * pageSize);
		createQuery.setMaxResults(pageSize);
		List<VTicketCount> resultList = createQuery.getResultList();
		return new PageImpl<VTicketCount>(resultList, page, resultList.size());
	}
	
	@Override
	public boolean ticketSave(Long productId,String unionid,String nickName,String headUrl){
		
		int count = phLotteryTicketRepository.editHead(productId, unionid, nickName, headUrl);
		if(count>0){
			return true;
		}
		return false;
	}
	
	@Override
	public void notice() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date endTime = sdf.parse(sdf.format(new Date()));
		String token = getToken();
		//查询当前时间是截至时间的活动
		List<PhProductInfo> phProductInfos = phProductInfoService.findByEndTime(endTime);
		for (PhProductInfo phProductInfo : phProductInfos) {
			notice(token, phProductInfo);
		}

	}

	@Override
	public void notice(String token, PhProductInfo phProductInfo) {
		List<Object> datas = phLotteryTicketRepository.findNoticeData(phProductInfo.getId());
		for (Object obj : datas) {
			Object[] data = (Object[]) obj;
			String openid = String.valueOf(data[0]);
			// 订阅消息包装
			Map<String, Object> args = WXUtil.buildMsg(openid, "pages/promo_reg/promo_reg.js?sharePath=true&id=" + phProductInfo.getId(), "开奖通知", phProductInfo.getName(), "活动已开奖，幸运儿是你吗？点击查看！");
			// 推送订阅消息
			WXUtil.sendSubscribeMsg(logger, args, token);
		}
	}

	/**
	 * 获取 access_token
	 */
	@Override
	public String getToken() {
		return WXUtil.getToken();
	}
}
