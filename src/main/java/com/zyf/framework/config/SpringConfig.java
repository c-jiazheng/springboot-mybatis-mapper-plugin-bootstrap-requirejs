package com.zyf.framework.config;

import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
//@ComponentScan(basePackages = {"com.zyf.admin.server.service"})
@ComponentScan
public class SpringConfig {


	@Bean
	public EhCacheManagerFactoryBean getEhCacheManagerFactoryBean(){
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource resource = resourcePatternResolver.getResource("classpath:/static/ehcache/testCache.xml");
		ehCacheManagerFactoryBean.setConfigLocation(resource);
		return ehCacheManagerFactoryBean;
	}

	@Bean("ehCacheCacheManager")
	public EhCacheCacheManager getEhCacheCacheManager(){
		EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
		ehCacheCacheManager.setCacheManager(getEhCacheManagerFactoryBean().getObject());
		return ehCacheCacheManager;
	}

}
