package cn.offway.hades.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.hades.domain.PhRole;

/**
 * Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhRoleService{

	PhRole save(PhRole phRole);
	
	PhRole findOne(Long id);

	Page<PhRole> findByPage(String name, Pageable page);

	void deleteRole(String ids) throws Exception;
}
