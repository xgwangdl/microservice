package com.accenture.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.accenture.common.auth.AuthInterceptors;

@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {

	/**
	 * 添加web拦截器
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		AuthInterceptors auth = new AuthInterceptors();
		registry.addInterceptor(auth);
	}
}
