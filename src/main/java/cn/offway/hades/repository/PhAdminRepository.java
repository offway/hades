package cn.offway.hades.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.offway.hades.domain.PhAdmin;

/**
 * Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhAdminRepository extends JpaRepository<PhAdmin,Long>,JpaSpecificationExecutor<PhAdmin> {

	PhAdmin findByUsername(String username);
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="delete from ph_admin where id in(?1)")
	int deleteByIds(List<Long> ids);
}
