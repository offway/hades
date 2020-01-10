package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhChannelUser;

/**
 * 用户推广渠道表Repository接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-01-10 11:33:13 Exp $
 */
public interface PhChannelUserRepository extends JpaRepository<PhChannelUser,Long>,JpaSpecificationExecutor<PhChannelUser> {

	/** 此处写一些自定义的方法 **/

	PhChannelUser findByUserId(Long id);
}
