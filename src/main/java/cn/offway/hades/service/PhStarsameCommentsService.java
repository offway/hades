package cn.offway.hades.service;


import cn.offway.hades.domain.PhStarsameComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 文章评论Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-18 16:00:10 Exp $
 */
public interface PhStarsameCommentsService {

    PhStarsameComments save(PhStarsameComments phActivityComments);

    PhStarsameComments findOne(Long id);

    void delete(Long id);

    List<PhStarsameComments> save(List<PhStarsameComments> entities);

    Page<PhStarsameComments> findAll(String pid, String id, String userId, String content, Pageable pageable);
}
