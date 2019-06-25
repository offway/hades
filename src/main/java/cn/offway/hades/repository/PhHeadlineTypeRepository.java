package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhHeadlineType;

/**
 * Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhHeadlineTypeRepository extends JpaRepository<PhHeadlineType,Long>,JpaSpecificationExecutor<PhHeadlineType> {

	/** 此处写一些自定义的方法 **/
}
