package com.zyf.framework.config;


import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 数据库配置
 */
@Configuration
public class DataSourceConfig {

	// Druid连接池
	// 连接池配置：https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8
	private DruidDataSource pool;

	@Bean(destroyMethod = "close")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		if (pool == null)
			pool = new DruidDataSource();
		return pool;
	}

	@PreDestroy
	public void close() {
		if (this.pool != null) {
			this.pool.close();
		}
	}

}
