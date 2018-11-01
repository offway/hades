package cn.offway.hades.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.offway.hades.domain.PhRoleresource;

/**
 * Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhRoleresourceRepository extends JpaRepository<PhRoleresource,Long>,JpaSpecificationExecutor<PhRoleresource> {

	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="delete from ph_roleresource where resource_id in(?1)")
	int deleteByResourceIds(List<Long> ids);
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="delete from ph_roleresource where role_id in(?1)")
	int deleteByRoleIds(List<Long> ids);
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="delete from ph_roleresource where role_id = ?1")
	int deleteByRoleId(Long id);
	
	@Query(nativeQuery=true,value="select resource_id from ph_roleresource where role_id = ?1")
	List<Long> findSourceIdByRoleId(Long roleId);
}
