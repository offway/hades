package cn.offway.hades.repository;

import cn.offway.hades.domain.PhUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户信息Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhUserInfoRepository extends JpaRepository<PhUserInfo, Long>, JpaSpecificationExecutor<PhUserInfo> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update ph_user_info set points=points+?2,version=version+1  where id=?1")
    int addPoints(Long id, Long points);
}
