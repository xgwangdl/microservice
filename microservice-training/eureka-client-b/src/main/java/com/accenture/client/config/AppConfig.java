package com.accenture.client.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.accenture.common.config.EurekaClientConfig;

@Configuration
@EnableAsync
@Import({EurekaClientConfig.class})
public class AppConfig {

	public static final String ASYNC_EXECUTOR_NAME = "clientB";
	
	@Bean(name=ASYNC_EXECUTOR_NAME)
	public Executor asyncExcecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(3);
		taskExecutor.setMaxPoolSize(5);
		taskExecutor.setQueueCapacity(20);
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		taskExecutor.setThreadNamePrefix("clientB-");
		taskExecutor.initialize();
		return taskExecutor;
	}
}
