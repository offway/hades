package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhCelebrityList;

/**
 * 明星信息表Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
public interface PhCelebrityListService{

    PhCelebrityList save(PhCelebrityList phCelebrityList);
	
    PhCelebrityList findOne(Long id);

    void delete(Long id);

    List<PhCelebrityList> save(List<PhCelebrityList> entities);
}
