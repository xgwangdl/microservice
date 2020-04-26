package com.accenture.common.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
	@Bean
	public Queue defaultQueue() {
		return new Queue("sendData", true, false, true);
	}
}
