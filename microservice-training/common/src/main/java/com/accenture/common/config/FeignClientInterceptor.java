package com.accenture.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.accenture.common.auth.FeignReqeustHandler;

@Configuration
public class FeignClientInterceptor {
	/**
	 * 添加feign拦截器
	 */
	@Bean
	public FeignReqeustHandler addRequestHandler() {
		return new FeignReqeustHandler();
	}
}
