package cn.offway.hades.service;


import java.util.List;

import cn.offway.hades.domain.PhAccumulatePoints;

/**
 * 积分记录Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-06 15:21:47 Exp $
 */
public interface PhAccumulatePointsService{

    PhAccumulatePoints save(PhAccumulatePoints phAccumulatePoints);
	
    PhAccumulatePoints findOne(Long id);

    void delete(Long id);

    List<PhAccumulatePoints> save(List<PhAccumulatePoints> entities);
}
