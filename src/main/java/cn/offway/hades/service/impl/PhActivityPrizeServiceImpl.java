package cn.offway.hades.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.offway.hades.service.PhActivityInfoService;
import cn.offway.hades.service.PhActivityJoinService;
import cn.offway.hades.service.PhActivityPrizeService;
import cn.offway.hades.utils.HttpClientUtil;
import cn.offway.hades.domain.PhActivityInfo;
import cn.offway.hades.domain.PhActivityJoin;
import cn.offway.hades.domain.PhActivityPrize;
import cn.offway.hades.domain.PhProductInfo;
import cn.offway.hades.dto.Template;
import cn.offway.hades.dto.TemplateParam;
import cn.offway.hades.repository.PhActivityPrizeRepository;


/**
 * 活动奖品表-每日福利Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhActivityPrizeServiceImpl implements PhActivityPrizeService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhActivityPrizeRepository phActivityPrizeRepository;
	
	@Autowired
	private PhActivityJoinService phActivityJoinService;
	
	@Autowired
	private PhActivityInfoService phActivityInfoService;
	
	@Override
	public PhActivityPrize save(PhActivityPrize phActivityPrize){
		return phActivityPrizeRepository.save(phActivityPrize);
	}
	
	@Override
	public PhActivityPrize findOne(Long id){
		return phActivityPrizeRepository.findOne(id);
	}
	
	@Override
	public PhActivityPrize findByActivityIdAndUnionid(Long activityid,String unionid){
		return phActivityPrizeRepository.findByActivityIdAndUnionid(activityid, unionid);
	}
	
	@Override
	public Page<PhActivityPrize> findByPage(final String activityName,final String nickName,final String unionid,final Long activityId,Pageable page){
		return phActivityPrizeRepository.findAll(new Specification<PhActivityPrize>() {
			
			@Override
			public Predicate toPredicate(Root<PhActivityPrize> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(activityName)){
					params.add(criteriaBuilder.like(root.get("activityName"), "%"+activityName+"%"));
				}
				
				if(null !=activityId){
					params.add(criteriaBuilder.equal(root.get("activityId"), activityId));
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
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
	public void open() throws Exception{
		/**
		 *  
		 * 1、查询参与用户
		 * 2、过滤黑名单
		 * 3、过滤已中奖用户
		 * 4、随机抽取
		 * 5、保存中奖记录
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date endTime = sdf.parse(sdf.format(new Date()));
		
		List<PhActivityInfo> phActivityInfos = phActivityInfoService.findByEndTime(endTime);
		
		String token = getToken();
		
		for (PhActivityInfo phActivityInfo : phActivityInfos) {
			
			Long windNum = phActivityInfo.getWinNum();
			//获得中奖用户
			List<PhActivityJoin> activityJoins = phActivityJoinService.luckly(phActivityInfo.getId(),windNum);
			
			//如果中奖用户不足，查询本活动参与人员中奖最早的
			int lessCount = windNum.intValue()-activityJoins.size();
			if(lessCount>0){
				List<PhActivityJoin> list = phActivityJoinService.findWinBefore(phActivityInfo.getId());
				int i=0;
				for (PhActivityJoin phActivityJoin : list) {
					if(i==lessCount){
						break;
					}
					boolean exists = false;
					for (PhActivityJoin aj : activityJoins) {
						if(aj.getUnionid().equals(phActivityJoin.getUnionid())){
							exists = true;
							break;
						}
					}
					if(!exists){
						activityJoins.add(phActivityJoin);
						i++;
					}
				}
			}
			
			
			Date now = new Date();
			List<PhActivityPrize> phActivityPrizes = new ArrayList<>();
			List<Long> ids = new ArrayList<>();
			for (PhActivityJoin phActivityJoin : activityJoins) {
				PhActivityPrize phActivityPrize = new PhActivityPrize();
				phActivityPrize.setActivityId(phActivityInfo.getId());
				phActivityPrize.setActivityImage(phActivityInfo.getImage());
				phActivityPrize.setActivityName(phActivityInfo.getName());
				phActivityPrize.setCreateTime(now);
				phActivityPrize.setHeadUrl(phActivityJoin.getHeadUrl());
				phActivityPrize.setNickName(phActivityJoin.getNickName());
				phActivityPrize.setStatus("0");
				phActivityPrize.setUnionid(phActivityJoin.getUnionid());
				
				ids.add(phActivityJoin.getId());
				phActivityPrizes.add(phActivityPrize);
			}
			
			phActivityJoinService.updateLuckly(ids);
			phActivityPrizeRepository.save(phActivityPrizes);
			//通知
			notice(token, phActivityInfo);
		}
		
	}
	
	public void notice(String token, PhActivityInfo phActivityInfo){
		try {
			List<Object> datas = phActivityJoinService.findNoticeData(phActivityInfo.getId());
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
				tem.setPage("pages/details/details?promotion_id="+phActivityInfo.getId()+"&sharePath=true");
				
				List<TemplateParam> paras = new ArrayList<TemplateParam>();
				paras.add(new TemplateParam("keyword1", "开奖通知", "#0044BB"));
				paras.add(new TemplateParam("keyword2", phActivityInfo.getName(), "#0044BB"));
				paras.add(new TemplateParam("keyword3", "得到福利的幸运儿是你吗？点击看看吧！", "#0044BB"));
				
				tem.setEmphasis_keyword("keyword1.DATA");
				
				tem.setTemplateParamList(paras);
				
				// 推送模版消息
				sendTemplateMsg(tem, token);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("每日福利开奖通知异常",e);
		}
	}

	/**
	 * 获取 access_token
	 */
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
