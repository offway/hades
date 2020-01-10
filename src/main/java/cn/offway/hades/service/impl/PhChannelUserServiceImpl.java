package cn.offway.hades.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhChannelUserService;

import cn.offway.hades.domain.PhChannelUser;
import cn.offway.hades.repository.PhChannelUserRepository;
import org.springframework.data.domain.Page;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * 用户推广渠道表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-01-10 11:33:13 Exp $
 */
@Service
public class PhChannelUserServiceImpl implements PhChannelUserService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhChannelUserRepository phChannelUserRepository;
	
	@Override
	public PhChannelUser save(PhChannelUser phChannelUser){
		return phChannelUserRepository.save(phChannelUser);
	}
	
	@Override
	public PhChannelUser findOne(Long id){
		return phChannelUserRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phChannelUserRepository.delete(id);
	}

	@Override
	public List<PhChannelUser> save(List<PhChannelUser> entities){
		return phChannelUserRepository.save(entities);
	}

	@Override
	public PhChannelUser findByUserId(Long id){
		return phChannelUserRepository.findByUserId(id);
	}

	@Override
	public Page<PhChannelUser> list(String channelName, String channel, String userId, String userPhone, Pageable pageable){
		return phChannelUserRepository.findAll(new Specification<PhChannelUser>() {
			@Override
			public Predicate toPredicate(Root<PhChannelUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				if (!"".equals(userPhone)) {
					params.add(criteriaBuilder.like(root.get("userPhone"), "%" + userPhone + "%"));
				}
				if (!"".equals(channelName)) {
					params.add(criteriaBuilder.like(root.get("channelName"), "%" + channelName + "%"));
				}
				if (!"".equals(channel)) {
					params.add(criteriaBuilder.equal(root.get("channel"), channel));
				}
				if (!"".equals(userId)) {
					params.add(criteriaBuilder.equal(root.get("id"), Long.valueOf(userId)));
				}
				Predicate[] predicates = new Predicate[params.size()];
				criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		},pageable);
	}
}
