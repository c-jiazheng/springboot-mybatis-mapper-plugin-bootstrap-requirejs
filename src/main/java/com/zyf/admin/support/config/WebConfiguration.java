package com.zyf.admin.support.config;

import com.zyf.admin.server.service.impl.sys.ResourceServiceImpl;
import com.zyf.admin.support.config.properties.SpringMvcProperties;
import com.zyf.admin.support.filter.URLPermissionsFilter;
import com.zyf.admin.support.interceptor.ShiroInterceptors;
import com.zyf.common.service.sys.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.DispatcherType;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan
@EnableConfigurationProperties(value = SpringMvcProperties.class)
public class WebConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private SpringMvcProperties springMvcProperties;
	private static final Charset UTF8 = Charset.forName("UTF-8");


	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebConfiguration.class);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		/*converters.add(mappingJackson2HttpMessageConverter());
		converters.add(stringHttpMessageConverter());*/
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(new MediaType("text", "plain", UTF8));
		supportedMediaTypes.add(new MediaType("application", "json", UTF8));
		jsonMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
		return jsonMessageConverter;
	}

	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(UTF8);
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(new MediaType("text", "plain", UTF8));
		supportedMediaTypes.add(new MediaType("*", "*", UTF8));
		stringHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
		return stringHttpMessageConverter;
	}

	/**
	 * 上传下载配置
	 * @return
	 */
	/*@Bean
	public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("50MB");
        factory.setMaxRequestSize("50MB");
        return factory.createMultipartConfig();
    }*/

	/**
	 * 直接访问jsp的路由
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		if (StringUtils.isNotBlank(this.springMvcProperties.getStaticViewName())) {
			String[] urls = this.springMvcProperties.getStaticViewName().split(";");
			for (String url : urls) {
				String[] viewName = url.split("=");
				registry.addViewController(viewName[0]).setViewName(viewName[1]);
			}
		}
		super.addViewControllers(registry);
	}

	/**
	 * 配置资源文件路由
	 * 配置文件已改
	 * spring.mvc.static-path-pattern=/static/**
	 * spring.resources.static-locations=/static/
	 */
	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String[] resourceUriAndPaths = this.springMvcProperties.getResourceUriAndPath().split(";");
		for (String resourceUriAndPath : resourceUriAndPaths) {
			String[] viewName = resourceUriAndPath.split("=");
			registry.addResourceHandler(viewName[0]).addResourceLocations(viewName[1]);
		}
		super.addResourceHandlers(registry);
	}*/

	/**
	 * 配置拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(new TestInterceptors()).excludePathPatterns("/static/**").addPathPatterns("/**");
		//registry.addInterceptor(shiroInterceptors()).excludePathPatterns("/static/**").addPathPatterns("/**");
	}
/*
	@Bean
	public ShiroInterceptors shiroInterceptors(){
		ShiroInterceptors shiroInterceptors = new ShiroInterceptors();
		return shiroInterceptors;
	}*/

}