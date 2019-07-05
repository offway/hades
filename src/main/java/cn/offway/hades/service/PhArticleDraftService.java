package cn.offway.hades.service;

import cn.offway.hades.domain.PhArticleDraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 文章Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhArticleDraftService {

    PhArticleDraft save(PhArticleDraft phArticle);

    PhArticleDraft findOne(Long id);

    Page<PhArticleDraft> findAll(Pageable pageable);

    void del(Long id);

    Page<PhArticleDraft> findAll(String name, String tag, String status, String title, String type, Pageable pageable);
}
