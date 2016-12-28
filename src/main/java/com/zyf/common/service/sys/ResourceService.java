package com.zyf.common.service.sys;

import com.zyf.common.entity.sys.Resource;

import java.util.List;
import java.util.Set;

public interface ResourceService {

	Resource queryById(long id);

	/**
	 * 根据等级查询
	 * @param resLevel 等级
	 * @return
	 */
	List<Resource> queryList(Integer... resLevel);

	List<Resource> queryList();

	List<Resource> queryList(List<String> ids);

	Set<String> queryPermissionByAccount(String account);
}
