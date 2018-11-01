package cn.offway.hades.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.offway.hades.domain.PhRoleadmin;

/**
 * Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhRoleadminRepository extends JpaRepository<PhRoleadmin,Long>,JpaSpecificationExecutor<PhRoleadmin> {

	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="delete from ph_roleadmin where admin_id in(?1)")
	int deleteByAdminIds(List<Long> ids);
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="delete from ph_roleadmin where role_id in(?1)")
	int deleteByRoleIds(List<Long> ids);
	
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="delete from ph_roleadmin where admin_id =?1")
	int deleteByAdminId(Long id);
	
	@Query(nativeQuery=true,value="select role_id from ph_roleadmin where admin_id =?1")
	List<Long> findRoleIdByAdminId(Long adminId);
	
}
