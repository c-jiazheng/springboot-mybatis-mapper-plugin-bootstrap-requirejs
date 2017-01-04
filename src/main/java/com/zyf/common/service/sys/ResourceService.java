package com.zyf.common.service.sys;

import com.zyf.common.entity.sys.Resource;

import java.util.List;
import java.util.Set;

public interface ResourceService {

	int insert(Resource resource);

	Resource queryById(long id);

	/**
	 * 根据等级查询
	 * @param resLevel 等级
	 * @return 资源集合
	 */
	List<Resource> queryList(Integer... resLevel);

	List<Resource> queryList();

	List<Resource> queryList(List<String> ids);

	/**
	 * 根据用户名查询角色可控制权限的标识集合
	 * @param account 账号
	 * @return 权限标识集合
	 */
	Set<String> queryPermissionByAccount(String account);
}
