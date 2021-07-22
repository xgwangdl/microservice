package com.accenture.common.okcatch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkCatchConfiguration {

	@Value("${okcatch.maxsize}")
	private String maxSize;
	
	@Bean
	public OkCatch<Object, Object> installOkCatch() {
		return new OkCatch<>(Integer.valueOf(this.maxSize));
	}
}
