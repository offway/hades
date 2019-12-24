package cn.offway.hades.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.offway.hades.domain.PhLotteryTicket;
import cn.offway.hades.domain.PhProductInfo;
import cn.offway.hades.dto.Template;
import cn.offway.hades.dto.TemplateParam;
import cn.offway.hades.dto.VTicketCount;
import cn.offway.hades.repository.PhLotteryTicketRepository;
import cn.offway.hades.service.PhLotteryTicketService;
import cn.offway.hades.service.PhProductInfoService;
import cn.offway.hades.utils.HttpClientUtil;




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
	public void notice(String token, PhProductInfo phProductInfo) throws Exception {
		List<Object> datas = phLotteryTicketRepository.findNoticeData(phProductInfo.getId());
		for (Object obj : datas) {
			Object[] data = (Object[])obj;
			String openid = String.valueOf(data[0]);
			String formid = String.valueOf(data[1]);
			
			// 模块消息配置
			Template tem = new Template();
			tem.setTemplateId("WTNwoOQEHWerZGTi9W-yLOjx4aavpxsArHtZoWBeLSQ");
			tem.setFormId(formid);
			tem.setTopColor("#00DD00");
			tem.setToUser(openid);
			tem.setPage("pages/promo_reg/promo_reg.js?sharePath=true&id="+phProductInfo.getId());
			
			List<TemplateParam> paras = new ArrayList<TemplateParam>();
			paras.add(new TemplateParam("keyword1", "开奖通知", "#0044BB"));
			paras.add(new TemplateParam("keyword2", phProductInfo.getName(), "#0044BB"));
			paras.add(new TemplateParam("keyword3", "活动已开奖，幸运儿是你吗？点击查看！", "#0044BB"));
			
			tem.setEmphasis_keyword("keyword1.DATA");
			
			tem.setTemplateParamList(paras);
			
			// 推送模版消息
			sendTemplateMsg(tem, token);
			
		}
	}

	/**
	 * 获取 access_token
	 */
	@Override
	public String getToken() {
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx12d022a9493f1b26&secret=52ba3a89ae58aa6a2294806d516d6107";
		String result = HttpClientUtil.post(requestUrl, "");
		JSONObject jsonObject = JSON.parseObject(result);
		if (jsonObject != null) {
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} else {
			return "";
		}
	}

	public void sendTemplateMsg(Template template, String token) {
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", token);
		String jsonString = template.toJSON();

		String result = HttpClientUtil.post(requestUrl, jsonString);
		JSONObject jsonResult = JSON.parseObject(result);
		if (jsonResult != null) {
			int errorCode = jsonResult.getIntValue("errcode");
			String errorMessage = jsonResult.getString("errmsg");
			if (errorCode == 0) {
				logger.info("模板消息发送成功:" + jsonResult);
			} else {
				logger.info("模板消息发送失败:" + errorCode + "," + errorMessage);
			}
		} else {
			logger.info("模板消息发送失败:" + jsonResult);
		}
	}
	
	

	
}
