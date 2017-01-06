package com.zyf.admin.support.filter;

import com.zyf.common.entity.sys.Resource;
import com.zyf.common.entity.sys.Role;
import com.zyf.common.entity.sys.User;
import com.zyf.common.service.sys.ResourceService;
import com.zyf.common.service.sys.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
public class URLPermissionsFilter implements Filter {

	public static final String STATIC_TAIL = "__oawx_t=";

	private static volatile boolean isUpdate = false;
	private Map<Long, List<Resource>> permissions = new HashMap<>();
	private List<String> urlList;

	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/**
	 * 只有get请求才拿url匹配
	 * //1，静态资源pass
	 * 2，一级二级菜单，逻辑处理pass
	 * 3，登录页面pass
	 * @param servletRequest
	 * @param servletResponse
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		String curUrl = getRequestUrl(servletRequest);
		//过滤登录及测试页面
		if(isLogin(curUrl) || isTest(curUrl)){
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		if(isUpdate){
			permissions.clear();
		}
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		//给静态资源加事件
		String queryStr = request.getQueryString();
		if(isResource(curUrl)) {
			String newURL = null;
			if (StringUtils.isNotBlank(queryStr) && queryStr.trim().indexOf(URLPermissionsFilter.STATIC_TAIL) == -1) {
				newURL = curUrl + "?" + queryStr + "&" + URLPermissionsFilter.STATIC_TAIL + new Date().getTime();
				response.sendRedirect(newURL);
				return;
			}
			if (StringUtils.isBlank(queryStr)) {
				newURL = curUrl + "?" + URLPermissionsFilter.STATIC_TAIL + new Date().getTime();
				response.sendRedirect(newURL);
				return;
			}
		}
		//监测urlList是否需要更新
		if(isUpdate || urlList==null){
			List<Resource> resourceList = resourceService.queryList(1,2);
			urlList = resourceList.parallelStream().map(Resource::getResUrl).collect(Collectors.toList());
			if(isUpdate)
				isUpdate = false;
		}
		//get 优先处理，非登录类型优先处理
		if("GET".equalsIgnoreCase(request.getMethod())) {
			if(SecurityUtils.getSubject().getPrincipals() == null) {//未登录
				filterChain.doFilter(request, response);
			}else if(urlList.toString().contains(curUrl)  //一二级链接
					||StringUtils.endsWithAny(curUrl, ".jsp")//jsp后缀
					|| curUrl.lastIndexOf(".") == -1){//无后缀
				List<Resource> resources;
				User user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
				if(permissions.containsKey(user.getRoleId())){
					resources = permissions.get(user.getRoleId());
				}else{
					Role role = roleService.query(user.getRoleId());
					if("all".equals(role.getResourceId())){
						resources = resourceService.queryList();
						permissions.put(user.getRoleId(), resources);
					}else{
						String[] split = role.getResourceId().split(",");
						resources = resourceService.queryList(Arrays.asList(split));
						permissions.put(user.getRoleId(), resources);
					}
				}
				request.setAttribute("resources", resources);
				filterChain.doFilter(request, response);
			}else{
				filterChain.doFilter(request, response);
			}
		}else{
			filterChain.doFilter(request, response);
		}
	}

	private boolean isTest(String curUrl) {
		Pattern compile = Pattern.compile("^/test/.*$");
		Matcher matcher = compile.matcher(curUrl);
		return matcher.matches();
	}

	private boolean isLogin(String curUrl) {
		Pattern compile = Pattern.compile("^/login.*$");
		Matcher matcher = compile.matcher(curUrl);
		return matcher.matches();
	}

	private boolean isResource(String curUrl) {
		Pattern compile = Pattern.compile("^/static/.*$");
		Matcher matcher = compile.matcher(curUrl);
		return curUrl.endsWith(".js") || curUrl.endsWith(".css") || matcher.matches();
	}

	@Override
	public void destroy() {

	}

	private String getRequestUrl(ServletRequest request) {
		HttpServletRequest req = (HttpServletRequest) request;
		return req.getRequestURI();
	}

}
