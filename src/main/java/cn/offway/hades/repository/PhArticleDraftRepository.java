package cn.offway.hades.repository;

import cn.offway.hades.domain.PhArticleDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhArticleDraftRepository extends JpaRepository<PhArticleDraft, Long>, JpaSpecificationExecutor<PhArticleDraft> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update  ph_article_draft set video = ?2 where video =?1 ")
    int updateVideoUrl(String video, String url);
}
