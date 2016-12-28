package com.zyf.admin.server.service.impl.sys;

import com.zyf.admin.server.mapper.sys.ResourceMapper;
import com.zyf.admin.server.mapper.sys.RoleMapper;
import com.zyf.admin.server.mapper.sys.UserMapper;
import com.zyf.common.base.mapper.AutoMapper;
import com.zyf.common.entity.sys.Resource;
import com.zyf.common.entity.sys.Role;
import com.zyf.common.entity.sys.User;
import com.zyf.common.service.sys.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private ResourceMapper resourceMapper;

	@Override
	public Resource queryById(long id) {
		Resource resource = resourceMapper.selectByPrimaryKey(id);
		return resource;
	}

	@Override
	public List<Resource> queryList(Integer... resLevel) {
		Example example = new Example(Resource.class);
		Example.Criteria criteria = example.createCriteria();
		if(resLevel.length>1)
			criteria.andIn("resLevel", Arrays.asList(resLevel));
		else
			criteria.andCondition("res_level=", resLevel[0]);
		List<Resource> resources = resourceMapper.selectByExample(example);
		return resources;
	}

	@Override
	public List<Resource> queryList() {
		List<Resource> resources = resourceMapper.selectAll();
		return resources;
	}

	@Override
	public List<Resource> queryList(List<String> ids) {
		Example example = new Example(Resource.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", ids);
		List<Resource> resources = resourceMapper.selectByExample(example);
		return resources;
	}

	@Override
	public Set<String> queryPermissionByAccount(String account) {
		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andCondition("account=", account);
		List<User> users = userMapper.selectByExample(example);
		if(users != null && !users.isEmpty()){
			Example roleExample = new Example(Role.class);
			Example.Criteria roleCriteria = roleExample.createCriteria();
			roleCriteria.andCondition("id=", users.get(0).getRoleId());
			List<Role> roles = roleMapper.selectByExample(roleExample);
			if(roles != null && !roles.isEmpty()){
				Example resourceExample = new Example(Resource.class);
				Example.Criteria resourceCriteria = resourceExample.createCriteria();
				resourceCriteria.andIn("id", Arrays.asList(roles.get(0).getResourceId().split(",")));
				List<Resource> resources = resourceMapper.selectByExample(resourceExample);
				return resources.parallelStream().map(Resource::getResPermission).collect(Collectors.toSet());
			}
		}
		return null;
	}

	private List<Object> relevanceQuery(AutoMapper[] mappers,
	                                    Class[] classes,
	                                    Object[] conditions,
	                                    Object[] args,
	                                    Class[] refType,
	                                    Object[] refs){
		Object invoke = null;
		for (int k=0; k<classes.length; k++){
			Example example = new Example(classes[k]);
			Example.Criteria criteria = example.createCriteria();
			for(int i=0; i<conditions.length; i++){
				if(invoke == null)
					criteria.andCondition(conditions[i] + "=", args[i]);
				else
					criteria.andCondition(refs[i] + "=", invoke);
			}
			List list = mappers[k].selectByExample(example);
			if(list != null && !list.isEmpty() && k >= classes.length-2) {
				Object o = list.get(0);
				try {
					String fieldName = refs[k].toString();
					String methodRealName = "get" + fieldName.replaceFirst(fieldName.substring(0, 1)
							, fieldName.toUpperCase().substring(0, 1));
					Method declaredMethod = classes[k].getDeclaredMethod(methodRealName, refType[k]);
					try {
						invoke = declaredMethod.invoke(o);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}else{
				return list;
			}
		}
		return null;
	}

}
