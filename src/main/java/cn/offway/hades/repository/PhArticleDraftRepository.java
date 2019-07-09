package cn.offway.hades.repository;

import cn.offway.hades.domain.PhArticleDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 文章Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhArticleDraftRepository extends JpaRepository<PhArticleDraft, Long>, JpaSpecificationExecutor<PhArticleDraft> {
}
