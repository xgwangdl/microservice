package com.accenture.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.accenture.common.exception.RestExceptionAdvice;

@Configuration
@Import({FeignClientInterceptor.class,WebInterceptorConfig.class,RestExceptionAdvice.class})
public class EurekaClientConfig {

}
