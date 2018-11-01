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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import cn.offway.hades.domain.PhResource;
import cn.offway.hades.domain.PhRole;
import cn.offway.hades.domain.PhRoleresource;
import cn.offway.hades.repository.PhResourceRepository;
import cn.offway.hades.repository.PhRoleRepository;
import cn.offway.hades.repository.PhRoleadminRepository;
import cn.offway.hades.repository.PhRoleresourceRepository;
import cn.offway.hades.service.PhRoleService;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhRoleServiceImpl implements PhRoleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhRoleRepository phRoleRepository;
	
	@Autowired
	private PhRoleresourceRepository phRoleresourceRepository;
	
	@Autowired
	private PhRoleadminRepository phRoleadminRepository;
	
	@Autowired
	private PhResourceRepository phResourceRepository;
	
	
	@Override
	public PhRole save(PhRole phRole){
		return phRoleRepository.save(phRole);
	}
	
	@Override
	public PhRole findOne(Long id){
		return phRoleRepository.findOne(id);
	}
	
	@Override
	public List<PhRole> findAll(){
		return phRoleRepository.findAll();
	}
	
	@Override
	public Page<PhRole> findByPage(final String name, Pageable page){
		return phRoleRepository.findAll(new Specification<PhRole>() {
			
			@Override
			public Predicate toPredicate(Root<PhRole> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(name)){
					params.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
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
	public void deleteRole(String ids) throws Exception{
		List<Long> idList = (List<Long>)ListUtils.toList(ids.split(","));
		phRoleadminRepository.deleteByRoleIds(idList);
		phRoleresourceRepository.deleteByRoleIds(idList);
		phRoleRepository.deleteByRoleIds(idList);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
	public void save(PhRole phRole,Long[] resourceIds) throws Exception{
		
		Date now = new Date();
		phRole.setCreatedtime(now);
		phRole = save(phRole);
		
		Long phRoleId = phRole.getId();
		
		List<Long> idList = (List<Long>)ListUtils.toList(resourceIds);
		List<PhResource> phResources = phResourceRepository.findByIds(idList);
		List<PhRoleresource> roleresources = new ArrayList<>();
		for (PhResource phResource : phResources) {
			getResources(now, phRoleId, roleresources, phResource);
		}
		phRoleresourceRepository.deleteByRoleId(phRoleId);
		phRoleresourceRepository.save(roleresources);
	}

	/**
	 * 查询父菜单
	 * @param now
	 * @param phRoleId
	 * @param roleresources
	 * @param phResource
	 */
	private void getResources(Date now, Long phRoleId, List<PhRoleresource> roleresources, PhResource phResource) {
		if(null != phResource.getParentId()){
			PhResource phResource1 = phResourceRepository.findOne(phResource.getParentId());
			getResources(now, phRoleId, roleresources, phResource1);
		}
		PhRoleresource phRoleresource = new PhRoleresource();
		phRoleresource.setCreatedtime(now);
		phRoleresource.setResourceId(phResource.getId());
		phRoleresource.setRoleId(phRoleId);
		roleresources.add(phRoleresource);
	}
}
