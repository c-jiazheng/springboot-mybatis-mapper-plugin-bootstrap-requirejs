package com.zyf.admin.web.common.realm;

import com.zyf.common.entity.sys.User;
import com.zyf.common.service.sys.ResourceService;
import com.zyf.common.service.sys.RoleService;
import com.zyf.common.service.sys.UserService;
import com.zyf.framework.utils.MD5;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 验证用户登录
 */
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private RoleService roleService;

	public UserRealm() {
		setName("userRealm");
		//加密策略
		setCredentialsMatcher(new CustomCredentialsMatcher());
	}

	//权限资源角色
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(resourceService.queryPermissionByAccount(username));
		//add Permission Resources
		//userService.findPermissions(username)
		//info.setStringPermissions(Arrays.asList("1,2,3,4,5,6,7,8,9".split(",")).parallelStream().collect(Collectors.toSet()));
		//add Roles String[Set<String> roles]
		//info.setRoles(roles);
		return info;
	}

	//登录验证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upt = (UsernamePasswordToken) token;
		String account = upt.getUsername();
		char[] password = upt.getPassword();
		User user = userService.login(account, MD5.encode(account + String.valueOf(password)));
		if(user == null)
			throw new UnknownAccountException();
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		return info;
	}
}