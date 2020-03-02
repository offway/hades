package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhFollow;

/**
 * 关注列表Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
public interface PhFollowService{

    PhFollow save(PhFollow phFollow);
	
    PhFollow findOne(Long id);

    void delete(Long id);

    List<PhFollow> save(List<PhFollow> entities);
}
