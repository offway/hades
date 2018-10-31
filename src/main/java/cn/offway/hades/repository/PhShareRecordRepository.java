package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhShareRecord;

/**
 * 分享记录表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhShareRecordRepository extends JpaRepository<PhShareRecord,Long>,JpaSpecificationExecutor<PhShareRecord> {

	/** 此处写一些自定义的方法 **/
}
