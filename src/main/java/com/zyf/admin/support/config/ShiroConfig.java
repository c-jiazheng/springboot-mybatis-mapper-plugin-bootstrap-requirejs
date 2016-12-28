package com.zyf.admin.support.config;

import com.google.common.collect.Maps;
import com.zyf.admin.support.config.properties.ShiroProperties;
import com.zyf.admin.support.filter.URLPermissionsFilter;
import com.zyf.admin.web.common.realm.UserRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(value = ShiroProperties.class)
public class ShiroConfig {

	@Autowired
	private ShiroProperties shiroProperties;

	/**
	 * FilterRegistrationBean
	 *
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns(this.shiroProperties.getUrlPatterns());
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
		return filterRegistration;
	}

	/**
	 * @return ShiroFilterFactoryBean
	 * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager());
		bean.setLoginUrl(this.shiroProperties.getLoginUrl());
		bean.setSuccessUrl(this.shiroProperties.getSuccessUrl());
		bean.setUnauthorizedUrl(this.shiroProperties.getUnauthorizedUrl());

		Map<String, Filter> filters = Maps.newHashMap();
		//filters.put("perms", urlPermissionsFilter());
		filters.put("anon", new AnonymousFilter());
		bean.setFilters(filters);

		Map<String, String> chains = Maps.newHashMap();
		if (this.shiroProperties.isEnableShrio()) {
			String filterChainDefinitions = this.shiroProperties.getFilterChainDefinitions();
			String[] filterChainSplit = filterChainDefinitions.split(";");
			for (String filterChains : filterChainSplit) {
				String[] filterChain = filterChains.split("=");
				String url = filterChain[0];
				String chain = filterChain[1];
				chains.put(url, chain);
			}
		}
		bean.setFilterChainDefinitionMap(chains);
		return bean;
	}

	/**
	 * @return DefaultWebSecurityManager
	 * @see org.apache.shiro.mgt.SecurityManager
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		manager.setRealm(userRealm());
		manager.setCacheManager(cacheManager());
		manager.setSessionManager(defaultWebSessionManager());
		return manager;
	}

	/**
	 * @return DefaultWebSessionManager
	 * @see org.apache.shiro.web.session.mgt.DefaultWebSessionManager
	 */
	@Bean(name = "sessionManager")
	public DefaultWebSessionManager defaultWebSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setCacheManager(cacheManager());
		sessionManager.setGlobalSessionTimeout(this.shiroProperties.getGlobalSessionTimeout());
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		return sessionManager;
	}

	/**
	 * @return UserRealm
	 * @see com.zyf.admin.web.common.realm.UserRealm --->AuthorizingRealm
	 */
	@Bean
	public UserRealm userRealm() {
		UserRealm userRealm = new UserRealm();
		userRealm.setCacheManager(cacheManager());
		return userRealm;
	}

	@Bean
	public URLPermissionsFilter urlPermissionsFilter() {
		return new URLPermissionsFilter();
	}

	@Bean
	public EhCacheManager cacheManager() {
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile(this.shiroProperties.getCacheManagerConfigFile());
		return cacheManager;
	}

}