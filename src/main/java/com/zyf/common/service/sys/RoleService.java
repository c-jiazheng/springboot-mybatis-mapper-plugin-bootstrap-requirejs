package com.zyf.common.service.sys;

import com.zyf.common.entity.sys.Role;
import com.zyf.common.entity.sys.User;
import com.zyf.common.option.sys.RoleOption;

import java.util.List;
import java.util.Set;

public interface RoleService {

	public Role query(long id);

	public List<Role> queryList(RoleOption roleOption);

}
