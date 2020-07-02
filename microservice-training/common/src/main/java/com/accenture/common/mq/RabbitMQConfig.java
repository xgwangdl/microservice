package com.accenture.common.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="spring.rabbitmq")
public class RabbitMQConfig {
	private String host;
	private Integer port;
	private String username;
	private String password;
	public static final String GATE_WAY_EXCHANGE = "microservice.gateway";
	public static final String GATE_WAY_QUEUE = "gatewayData";
	public static final String GATE_WAY_POINT_KEY = "gateWay";
	
	public static final String COUNT_QUEUE = "countData";
	public static final String COUNT_EXCHANGE = "microservice.count";
	public static final String COUNT_POINT_KEY = "count";
	
	@Bean
	public Queue gateWayQueue() {
		return new Queue(GATE_WAY_QUEUE, true, false, true);
	}

	@Bean
	public Queue countQueue() {
		return new Queue(COUNT_QUEUE, true, false, true);
	}
	
	@Bean
	public DirectExchange gatewayExchange() {
		return new DirectExchange(GATE_WAY_EXCHANGE, true, false);
	}

	@Bean
	public DirectExchange countExchange() {
		return new DirectExchange(COUNT_EXCHANGE, true, false);
	}
	
	@Bean
	public Binding gatewayBinding() {
		return BindingBuilder.bind(gateWayQueue()).to(gatewayExchange()).with(GATE_WAY_POINT_KEY);
	}

	@Bean
	public Binding countBinding() {
		return BindingBuilder.bind(countQueue()).to(countExchange()).with(COUNT_POINT_KEY);
	}
	
	@Bean
	public ConnectionFactory connectionFactory() {
	    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.host,this.port);

	    connectionFactory.setUsername(this.username);
	    connectionFactory.setPassword(this.password);
	    connectionFactory.setVirtualHost("/");
	    connectionFactory.setPublisherConfirms(true);

	    return connectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
	    RabbitTemplate template = new RabbitTemplate(connectionFactory());
	    return template;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
