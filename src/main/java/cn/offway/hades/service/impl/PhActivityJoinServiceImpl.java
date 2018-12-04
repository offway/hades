package cn.offway.hades.service.impl;

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
import org.springframework.web.bind.annotation.RequestParam;

import cn.offway.hades.domain.PhActivityInfo;
import cn.offway.hades.domain.PhActivityJoin;
import cn.offway.hades.domain.PhWxuserInfo;
import cn.offway.hades.repository.PhActivityJoinRepository;
import cn.offway.hades.service.PhActivityJoinService;


/**
 * 活动参与表-每日福利Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhActivityJoinServiceImpl implements PhActivityJoinService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhActivityJoinRepository phActivityJoinRepository;
	
	@Override
	public PhActivityJoin save(PhActivityJoin phActivityJoin){
		return phActivityJoinRepository.save(phActivityJoin);
	}
	
	@Override
	public PhActivityJoin findOne(Long id){
		return phActivityJoinRepository.findOne(id);
	}
	
	@Override
	public int countByUnionidAndActivityId(String unionid,Long activityId){
		return phActivityJoinRepository.countByUnionidAndActivityId(unionid, activityId);
	}
	
	@Override
	public void join(PhActivityInfo phActivityInfo,PhWxuserInfo phWxuserInfo ){
		
		
		PhActivityJoin phActivityJoin = new PhActivityJoin();
		phActivityJoin.setActivityId(phActivityInfo.getId());
		phActivityJoin.setActivityImage(phActivityInfo.getImage());
		phActivityJoin.setActivityName(phActivityInfo.getName());
		phActivityJoin.setCreateTime(new Date());
		phActivityJoin.setHeadUrl(phWxuserInfo.getHeadimgurl());
		phActivityJoin.setIsLucky("0");
		phActivityJoin.setNickName(phWxuserInfo.getNickname());
		phActivityJoin.setRemark("");
		phActivityJoin.setUnionid(phWxuserInfo.getUnionid());
		
		save(phActivityJoin);
		
	}
	
	@Override
	public List<PhActivityJoin> findByActivityId(Long activityId){
		return phActivityJoinRepository.findByActivityId(activityId);
	}
	
	@Override
	public List<PhActivityJoin> luckly(Long activityId,Long num){
		return phActivityJoinRepository.luckly(activityId, num);
	}
	
	@Override
	public int updateLuckly(List<Long> ids){
		return phActivityJoinRepository.updateLuckly(ids);
	}
	
	@Override
	public List<Object> findNoticeData(Long activityId){
		return phActivityJoinRepository.findNoticeData(activityId);
	}
	
	@Override
	public List<PhActivityJoin> findWinBefore(Long activityId){
		return phActivityJoinRepository.findWinBefore(activityId);
	}
	
	@Override
	public Page<PhActivityJoin> findByPage(final String activityName,final String nickName,final String unionid,final Long activityId,Pageable page){
		return phActivityJoinRepository.findAll(new Specification<PhActivityJoin>() {
			
			@Override
			public Predicate toPredicate(Root<PhActivityJoin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(activityName)){
					params.add(criteriaBuilder.like(root.get("activityName"), "%"+activityName+"%"));
				}
				
				if(null != activityId){
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
}
