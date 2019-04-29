package cn.offway.hades.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.offway.hades.domain.PhResource;

/**
 * Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhResourceRepository extends JpaRepository<PhResource,Long>,JpaSpecificationExecutor<PhResource> {

	@Query(nativeQuery = true,value="select * from ph_resource where id in (select DISTINCT(ru.id) from ph_resource ru ,ph_roleresource rr,ph_roleadmin ra where ru.id = rr.resource_id and rr.role_id = ra.role_id and ra.admin_id=?1 ) ORDER BY parent_id ASC,sort ASC")
	List<PhResource> findByAdminId(Long adminId);
	
	@Query(nativeQuery = true,value="select DISTINCT(ru.link) from ph_resource ru ,ph_roleresource rr,ph_roleadmin ra where ru.id = rr.resource_id and rr.role_id = ra.role_id and ra.admin_id=?1")
	Set<String> findUrlsByAdminId(Long adminId);
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="delete from ph_resource where id in(?1)")
	int deleteByIds(List<Long> ids);
	
	List<PhResource> findByParentId(Long parentId);
	
	List<PhResource> findByParentIdNotNull();
	
	@Query(nativeQuery=true,value="select * from ph_resource where id in(?1)")
	List<PhResource> findByIds(List<Long> ids);
}
