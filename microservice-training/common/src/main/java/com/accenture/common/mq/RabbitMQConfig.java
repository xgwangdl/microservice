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
