package com.boot.db;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component("dataSource")
@Configuration
@PropertySource(value = {"classpath:dataSource.properties"},encoding="utf-8")
public class DynamicDataSource extends AbstractRoutingDataSource {
	
	private DataSource dataSourceMaster;
	private DataSource dataSourceSlave;

	@Value("${master.datasource.url}")
	private String master_url;
	@Value("${master.datasource.username}")
	private String master_username;
	@Value("${master.datasource.password}")
	private String master_password;

	// 添加两个数据源设置默认为主
	@PostConstruct
	public void init(){
		try {
			Properties masterProperties = new Properties();
			masterProperties.setProperty("url", master_url);
			masterProperties.setProperty("username", master_username);
			masterProperties.setProperty("password", master_password);
			dataSourceMaster = DruidDataSourceFactory.createDataSource(masterProperties);
			dataSourceSlave = DruidDataSourceFactory.createDataSource(masterProperties);
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("dataSource_RW", dataSourceMaster);
			map.put("dataSource_R", dataSourceMaster);
			setTargetDataSources(map);
			setDefaultTargetDataSource(dataSourceMaster);
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("加载数据源失败！！！");
		}
	}

	// 根据指定的key获取对应的map里面的key，获取对应的数据源，与前面的map对应
	@Override
	protected Object determineCurrentLookupKey() {
		String dataSource = DBContextHolder.getDbType();
		return dataSource;
	}

}
