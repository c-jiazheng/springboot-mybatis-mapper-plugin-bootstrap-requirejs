package com.zyf.admin.support.interceptor;

import com.zyf.common.entity.sys.Resource;
import com.zyf.common.entity.sys.Role;
import com.zyf.common.entity.sys.User;
import com.zyf.common.service.sys.ResourceService;
import com.zyf.common.service.sys.RoleService;
import com.zyf.common.service.sys.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration//若不配置该项，则无法注入
public class ShiroInterceptors implements HandlerInterceptor {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static Map<Long, List<Resource>> permissions = new HashMap<>();

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("queryString:{{}}, uri:{{}}", request.getQueryString(), request.getRequestURI());
		Pattern p = Pattern.compile("/login");
		Matcher m = p.matcher(request.getRequestURI());
		if(m.find()){
			return true;
		}
		List<Resource> resources;
		User user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
		if(permissions.containsKey(user.getRoleId())){
			resources = permissions.get(user.getRoleId());
		}else{
			Role role = roleService.query(user.getRoleId());
			String[] split = role.getResourceId().split(",");
			resources = resourceService.queryList(Arrays.asList(split));
			permissions.put(user.getRoleId(), resources);
		}
		request.setAttribute("resources", resources);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception { }

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
