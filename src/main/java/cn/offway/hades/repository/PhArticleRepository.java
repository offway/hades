package cn.offway.hades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.hades.domain.PhArticle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhArticleRepository extends JpaRepository<PhArticle,Long>,JpaSpecificationExecutor<PhArticle> {

	@Transactional
	@Modifying
	@Query(nativeQuery = true,value = "update  ph_article set video = ?2 where video =?1 ")
	int updateVideoUrl(String video,String url);
}
