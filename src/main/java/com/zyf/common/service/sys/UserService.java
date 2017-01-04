package com.zyf.common.service.sys;

import com.zyf.common.entity.sys.User;

import java.util.List;

public interface UserService {

	/**
	 * 根据账号Account、Password 登录，空则登录失败
	 * @param account 账户
	 * @param password 密码
	 * @return User
	 */
	User login(String account, String password);

	/**
	 * 查询用户列表
	 * @return User
	 */
	List<User> queryList();

	/**
	 * 根据账号Account查询当前用户
	 * @param account 账户
	 * @return User
	 */
	User queryByAccont(String account);

	/**
	 * 新增用户
	 * @param user 用户实体
	 * @return int 成功数目
	 */
	int insert(User user);

	/**
	 * 修改用户
	 * @param user 用户实体
	 * @return int 成功数目
	 */
	int update(User user);
}
