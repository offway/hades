package cn.offway.hades.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import org.thymeleaf.util.ListUtils;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhResource;
import cn.offway.hades.repository.PhResourceRepository;
import cn.offway.hades.repository.PhRoleresourceRepository;
import cn.offway.hades.service.PhResourceService;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhResourceServiceImpl implements PhResourceService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhResourceRepository phResourceRepository;
	
	@Autowired
	private PhRoleresourceRepository phRoleresourceRepository;
	
	@Override
	public PhResource save(PhResource phResource){
		return phResourceRepository.save(phResource);
	}
	
	@Override
	public PhResource findOne(Long id){
		return phResourceRepository.findOne(id);
	}
	
	@Override
	public Set<String> findUrlsByAdminId(Long adminId){
		return phResourceRepository.findUrlsByAdminId(adminId);
	}
	
	@Override
	public List<PhResource> findByAdminId(Long adminId){
		return phResourceRepository.findByAdminId(adminId);
	}
	
	@Override
	public Page<PhResource> findByPage(final String name,final String link,final Long parentId, Pageable page){
		return phResourceRepository.findAll(new Specification<PhResource>() {
			
			@Override
			public Predicate toPredicate(Root<PhResource> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(name)){
					params.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
				}
				
				if(StringUtils.isNotBlank(link)){
					params.add(criteriaBuilder.equal(root.get("link"), link));
				}
				
				if(null != parentId){
					params.add(criteriaBuilder.equal(root.get("parentId"), parentId));
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				
				return null;
			}
		}, page);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
	public void deleteResource(String ids) throws Exception{
		List<Long> idList = (List<Long>)ListUtils.toList(ids.split(","));
		phRoleresourceRepository.deleteByResourceIds(idList);
		phResourceRepository.deleteByIds(idList);
	}
	
	@Override
	public List<PhResource> findByParentId(Long parentId){
		return phResourceRepository.findByParentId(parentId);
	}
	
	@Override
	public List<PhResource> findByParentIdNotNull(){
		return phResourceRepository.findByParentIdNotNull();
	}
}
