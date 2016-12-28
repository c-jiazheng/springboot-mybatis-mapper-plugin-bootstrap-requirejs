package com.zyf.admin.server.service.impl.sys;

import com.zyf.admin.server.mapper.sys.UserMapper;
import com.zyf.common.entity.sys.User;
import com.zyf.common.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public User login(String account, String password) {
		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andCondition("account=", account);
		criteria.andCondition("password=", password);
		List<User> users = userMapper.selectByExample(example);
		if(users !=null && !users.isEmpty())
			return users.get(0);
		else
			return null;
	}

	@Override
	public List<User> queryList() {
		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();
		List<User> users = userMapper.selectByExample(example);
		if(users !=null && !users.isEmpty())
			return users;
		else
			return null;
	}

	@Override
	public User queryByAccont(String account) {
		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andCondition("account=", account);
		List<User> users = userMapper.selectByExample(example);
		if(users !=null && !users.isEmpty())
			return users.get(0);
		else
			return null;
	}

	@Override
	public int insert(User user) {
		user.setCreateTime(Calendar.getInstance());
		return userMapper.insertSelective(user);
	}

}