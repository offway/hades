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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhRoleadmin;
import cn.offway.hades.repository.PhAdminRepository;
import cn.offway.hades.repository.PhRoleadminRepository;
import cn.offway.hades.service.PhAdminService;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhAdminServiceImpl implements PhAdminService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhAdminRepository phAdminRepository;
	
	@Autowired
	private PhRoleadminRepository phRoleadminRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${ph.default.pwd}")
	private String DEFAULT_PWD;
	
	@Override
	public PhAdmin save(PhAdmin phAdmin){
		return phAdminRepository.save(phAdmin);
	}
	
	@Override
	public PhAdmin findOne(Long id){
		return phAdminRepository.findOne(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
	public void deleteAdmin(String ids) throws Exception{
		List<Long> idList = (List<Long>)ListUtils.toList(ids.split(","));
		phRoleadminRepository.deleteByAdminIds(idList);
		phAdminRepository.deleteByIds(idList);
	}
	
	@Override
	public Page<PhAdmin> findByPage(final String username,final String nickname, Pageable page){
		return phAdminRepository.findAll(new Specification<PhAdmin>() {
			
			@Override
			public Predicate toPredicate(Root<PhAdmin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(username)){
					params.add(criteriaBuilder.like(root.get("username"), "%"+username+"%"));
				}
				
				if(StringUtils.isNotBlank(nickname)){
					params.add(criteriaBuilder.like(root.get("nickname"), "%"+nickname+"%"));
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				
				return null;
			}
		}, page);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
	public void save(PhAdmin phAdmin,Long[] roleIds){
		
		Date now = new Date();
		phAdmin.setCreatedtime(now);
		phAdmin.setPassword(passwordEncoder.encode(DEFAULT_PWD));
		phAdmin = save(phAdmin);
		
		Long adminId = phAdmin.getId();
		
		List<PhRoleadmin> phRoleadmins = new ArrayList<>();
		for (Long roleId : roleIds) {
			PhRoleadmin phRoleadmin = new PhRoleadmin();
			phRoleadmin.setAdminId(adminId);
			phRoleadmin.setCreatedtime(now);
			phRoleadmin.setRoleId(roleId);
			phRoleadmins.add(phRoleadmin);
		}
		
		phRoleadminRepository.deleteByAdminId(adminId);
		phRoleadminRepository.save(phRoleadmins);
		
	}
	
	@Override
	public void resetPwd(Long id) throws Exception{
		PhAdmin phAdmin = findOne(id);
		phAdmin.setPassword(passwordEncoder.encode(DEFAULT_PWD));
		save(phAdmin);
	}
	
	@Override
	public boolean editPwd(Long id,String password,String newpassword) throws Exception{
		PhAdmin phAdmin = findOne(id);
		if(passwordEncoder.matches(password, phAdmin.getPassword())){
			phAdmin.setPassword(passwordEncoder.encode(newpassword));
			save(phAdmin);
			return true;
		}else{
			return false;
		}
	}
}
