package cn.offway.hades.utils;

import java.util.List;

/**
 * 树形数据实体接口
 * 
 * @param <E>
 * @author jianda
 * @date 2017年5月26日
 */
public interface TreeEntity<E> {
	
	public Long getId();

	public Long getParentId();

	public void setChildList(List<E> childList);
}