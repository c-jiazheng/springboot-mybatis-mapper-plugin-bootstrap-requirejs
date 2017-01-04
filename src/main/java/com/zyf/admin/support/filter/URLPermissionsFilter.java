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
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class URLPermissionsFilter implements Filter {

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
		if(isUpdate){
			permissions.clear();
		}
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		if(isUpdate || urlList==null){//urlList的更新，需要知道公共版本号(version+timer)。
			List<Resource> resourceList = resourceService.queryList(1,2);
			urlList = resourceList.parallelStream().map(Resource::getResUrl).collect(Collectors.toList());
			if(isUpdate)
				isUpdate = false;
		}
		//get 优先处理，非登录类型优先处理
		if("GET".equalsIgnoreCase(request.getMethod())) {
			String curUrl = getRequestUrl(servletRequest);
			if(SecurityUtils.getSubject().getPrincipals() == null) {//未登录
				filterChain.doFilter(servletRequest, servletResponse);
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
				filterChain.doFilter(servletRequest, servletResponse);
			}else{
				filterChain.doFilter(servletRequest, servletResponse);
			}
		}else{
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void destroy() {

	}

	private String getRequestUrl(ServletRequest request) {
		HttpServletRequest req = (HttpServletRequest) request;
		String queryString = req.getQueryString();
		return req.getRequestURI();
	}

}
