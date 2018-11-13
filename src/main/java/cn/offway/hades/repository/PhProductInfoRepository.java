package cn.offway.hades.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhProductInfo;

/**
 * 活动产品表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhProductInfoRepository extends JpaRepository<PhProductInfo,Long>,JpaSpecificationExecutor<PhProductInfo> {

	List<PhProductInfo> findByEndTime(Date endTime);
}
