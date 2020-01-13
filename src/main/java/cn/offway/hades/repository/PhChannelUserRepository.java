package cn.offway.hades.repository;

import cn.offway.hades.domain.PhChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * 用户推广渠道表Repository接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-01-10 11:33:13 Exp $
 */
public interface PhChannelUserRepository extends JpaRepository<PhChannelUser, Long>, JpaSpecificationExecutor<PhChannelUser> {
    PhChannelUser findByUserId(Long id);

    @Query(nativeQuery = true, value = "SELECT count(id) FROM ph_user_info where (`channel` = ?1)")
    Optional<String> statUsers(String channel);

    @Query(nativeQuery = true, value = "SELECT sum(price),count(id) FROM ph_order_info where user_id in (SELECT id FROM ph_user_info where channel = ?1) and status in (1,2,3) and order_no not in (SELECT order_no FROM ph_refund where status = 4)")
    Object[] statOrder(String channel);
}
