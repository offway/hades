package cn.offway.hades.service;


import cn.offway.hades.domain.PhCelebrityList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 明星信息表Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
public interface PhCelebrityListService {

    PhCelebrityList save(PhCelebrityList phCelebrityList);

    PhCelebrityList findOne(Long id);

    void delete(Long id);

    List<PhCelebrityList> save(List<PhCelebrityList> entities);

    Page<PhCelebrityList> list(String name, Pageable pageable);

    List<Object[]> list(String name, int offset);

    long count(String name);

    List<PhCelebrityList> findAll();
}
