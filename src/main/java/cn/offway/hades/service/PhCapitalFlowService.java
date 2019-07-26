package cn.offway.hades.service;

import cn.offway.hades.domain.PhCapitalFlow;

import java.util.List;

/**
 * 资金流水Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhCapitalFlowService{

	PhCapitalFlow save(PhCapitalFlow phCapitalFlow);
	
	PhCapitalFlow findOne(Long id);

	List<PhCapitalFlow> finAllByuseridList(Long userid);

	List<PhCapitalFlow> finAllByamountFlowing();
}
