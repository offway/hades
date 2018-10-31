/**
 * 
 */
package cn.offway.hades.authentication;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhResource;
import cn.offway.hades.repository.PhAdminRepository;
import cn.offway.hades.service.PhResourceService;
import cn.offway.hades.utils.TreeParser;

/**
 * @author wn
 *
 */
@Component
@Transactional
public class RbacUserDetailsService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PhAdminRepository phAdminRepository;
	
	@Autowired
	private PhResourceService phResourceService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("表单登录用户名:" + username);
		PhAdmin admin = phAdminRepository.findByUsername(username);
		if(null == admin){
			throw new UsernameNotFoundException("用户名不正确");
		}
		//菜单
		List<PhResource> resources = phResourceService.findByAdminId(admin.getId());
		admin.setResources(TreeParser.getTreeList(0L, resources));
		
		//权限
		Set<String> urls = new HashSet<>();
		for (PhResource phResource : resources) {
			urls.add(phResource.getLink());
		}
		
		admin.setUrls(urls);
		return admin;
	}

}
