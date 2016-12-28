package com.zyf.framework.config;

import com.github.pagehelper.PageHelper;
import com.zyf.framework.config.properties.MybatisProperties;
import com.zyf.framework.plugin.MapperRefresh;
import com.zyf.framework.plugin.PerformanceInterceptor;
import com.zyf.framework.plugin.SqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-Configuration} for Mybatis. Contributes a
 * {@link org.apache.ibatis.session.SqlSessionFactory} and a {@link org.mybatis.spring.SqlSessionTemplate}.
 * <p>
 * If {@link org.mybatis.spring.annotation.MapperScan} is used, or a configuration file is
 * specified as a property, those will be considered, otherwise this auto-configuration
 * will attempt to register mappers based on the interface definitions in or under the
 * root auto-configuration package.
 *
 * @author Eddú Meléndez
 * @author Josh Long
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, org.mybatis.spring.SqlSessionFactoryBean.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(MybatisProperties.class)
@EnableTransactionManagement
public class MybatisAutoConfiguration implements TransactionManagementConfigurer {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private MybatisProperties properties;

	@Autowired
	DataSource dataSource;

	@Autowired(required = false)
	private Interceptor[] interceptors;

	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	@PostConstruct
	public void checkConfigFileExists() {
		if (this.properties.isCheckConfigLocation()) {
			Resource resource = this.resourceLoader
					.getResource(this.properties.getConfigLocation());
			Assert.state(resource.exists(),
					"Cannot find support location: " + resource
							+ " (please add support file or check your Mybatis "
							+ "configuration)");
		}
	}

	@Bean(name = "sqlSessionFactory")
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			factory.setConfigLocation(
					this.resourceLoader.getResource(this.properties.getConfigLocation()));
		} else {
			if (this.interceptors != null && this.interceptors.length > 0) {
				factory.setPlugins(this.interceptors);
			}
			factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
			factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			factory.setMapperLocations(resolver.getResources(this.properties.getMapperLocations()));
			//刷新mapper.xml
			MapperRefresh mapperRefresh = new MapperRefresh();
			mapperRefresh.setEnabled(this.properties.isMapperRefreshEnable());
			mapperRefresh.setMappingPath(this.properties.getMapperRefreshPath());
			factory.setMapperRefresh(mapperRefresh);
		}
		return factory.getObject();
	}

	/**
	 * 用于实际查询的sql工具,传统dao开发形式可以使用这个,基于mapper代理则不需要注入
	 */
	@Bean
	@ConditionalOnMissingBean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory,
				this.properties.getExecutorType());
	}

	/**
	 * 分页插件
	 *
	 * @return PageHelper
	 * @author SHANHY
	 */
	@Bean
	public PageHelper pageHelper() {
		log.info("注册MyBatis分页插件PageHelper");
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}

	/**
	 * 性能显示插件打印SQL
	 */
	@Bean
	public PerformanceInterceptor performanceInterceptor() {
		log.info("注册性能显示插件PerformanceInterceptor");
		return new PerformanceInterceptor();
	}

	/**
	 * 事务管理,具体使用在service层加入@Transactional注解
	 */
	@Bean(name = "transactionManager")
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

}

