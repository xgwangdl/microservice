package com.accenture.common.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig  extends CachingConnectionFactory  {
	public void setPaasword(String password) {
		super.setPassword(password+"@163");
	}
	
	public static final String DEFAULT_DIRECT_EXCHANGE = "microservice.clienta";
	public static final String DEFAULT_QUEUE = "sendData";
	public static final String POINT_KEY = "microservice.clienta-key";

	@Bean
	public Queue defaultQueue() {
		return new Queue(DEFAULT_QUEUE, true, false, true);
	}

	@Bean
	public DirectExchange defaultExchange() {
		return new DirectExchange(DEFAULT_DIRECT_EXCHANGE, true, false);
	}

	@Bean
	public Binding defaultBinding() {
		return BindingBuilder.bind(defaultQueue()).to(defaultExchange()).with(POINT_KEY);
	}

}
