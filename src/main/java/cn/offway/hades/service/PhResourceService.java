package cn.offway.hades.service;

import java.util.List;
import java.util.Set;

import cn.offway.hades.domain.PhResource;

/**
 * Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhResourceService{

	PhResource save(PhResource phResource);
	
	PhResource findOne(Long id);

	Set<String> findUrlsByAdminId(Long adminId);

	List<PhResource> findByAdminId(Long adminId);
}
