package com.accenture.common.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.accenture.common.auth.AuthInterceptors;
import com.accenture.common.auth.FeignReqeustHandler;

@Configuration
public class ServiceConfig implements WebMvcConfigurer {

	/**
	 * 添加web拦截器
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		AuthInterceptors auth = new AuthInterceptors();
		registry.addInterceptor(auth);
	}
	
	/**
	 * 添加feign拦截器
	 */
	@Bean
	public FeignReqeustHandler addRequestHandler() {
		return new FeignReqeustHandler();
	}
}
