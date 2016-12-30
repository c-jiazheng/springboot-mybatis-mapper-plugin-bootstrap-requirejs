package com.zyf.admin.server.service.impl.sys;

import com.zyf.admin.server.mapper.sys.RoleMapper;
import com.zyf.admin.server.mapper.sys.UserMapper;
import com.zyf.common.base.mapper.AutoMapper;
import com.zyf.common.entity.sys.Role;
import com.zyf.common.entity.sys.User;
import com.zyf.common.option.sys.RoleOption;
import com.zyf.common.service.sys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public int insert(Role role) {
		role.setCreateTime(Calendar.getInstance());
		return roleMapper.insert(role);
	}

	@Override
	public int update(Role role) {
		return roleMapper.updateByPrimaryKeySelective(role);
	}

	@Override
	public int updateRoleResource(Role role) {
		Example example = new Example(Role.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", role.getId());
		return roleMapper.updateByExampleSelective(role, example);
	}

	@Override
	public Role query(long id) {
		return roleMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Role> queryList(RoleOption roleOption) {
		Example example = new Example(Role.class);
		Example.Criteria criteria = example.createCriteria();
		return roleMapper.selectByExample(example);
	}

}