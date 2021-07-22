package com.accenture.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.accenture.common.exception.RestExceptionAdvice;
import com.accenture.globaltransaction.aspect.ConnectionAspect;
import com.accenture.globaltransaction.aspect.GlobalTransactionAspect;

@Configuration
@Import({ FeignClientInterceptor.class, WebInterceptorConfig.class, RestExceptionAdvice.class,
		GlobalTransactionAspect.class, ConnectionAspect.class })
public class EurekaClientConfig {

}
