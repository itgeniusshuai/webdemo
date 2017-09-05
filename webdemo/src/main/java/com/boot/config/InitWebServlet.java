package com.boot.config;

import com.boot.StartApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


public class InitWebServlet extends SpringBootServletInitializer {

	/**
	 * tomcat 中启动会使用到
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		//TODO 读取服务器的配置文件
		return builder.sources(StartApplication.class);
	}

}
