package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhActivityInfo;
import cn.offway.hades.domain.PhActivityJoin;
import cn.offway.hades.domain.PhActivityPrize;
import cn.offway.hades.repository.PhActivityPrizeRepository;
import cn.offway.hades.service.PhActivityInfoService;
import cn.offway.hades.service.PhActivityJoinService;
import cn.offway.hades.service.PhActivityPrizeService;
import cn.offway.hades.utils.WXUtil;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
			activityOpen(token, phActivityInfo);
		}
		
	}

	@Override
	public void activityOpen(String token, PhActivityInfo phActivityInfo) {
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

	public void notice(String token, PhActivityInfo phActivityInfo) {
		try {
			List<Object> datas = phActivityJoinService.findNoticeData(phActivityInfo.getId());
			for (Object obj : datas) {
				Object[] data = (Object[]) obj;
				String openid = String.valueOf(data[0]);
				// 订阅消息包装
				Map<String, Object> args = WXUtil.buildMsg(openid, "pages/details/details?promotion_id=" + phActivityInfo.getId() + "&sharePath=true", "开奖通知", phActivityInfo.getName(), "得到福利的幸运儿是你吗？点击看看吧！");
				// 推送订阅消息
				WXUtil.sendSubscribeMsg(logger, args, token);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("每日福利开奖通知异常", e);
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
