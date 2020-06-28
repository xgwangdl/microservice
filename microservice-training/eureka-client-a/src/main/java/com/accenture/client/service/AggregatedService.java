package com.accenture.client.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.accenture.client.config.AppConfig;

@Service
public class AggregatedService {

	@Autowired
	private RabbitTemplate amqpTemplate;
	
	// 计数器
	private static Map<String,Integer> countMap = new HashMap<>();
	
	@Async(AppConfig.ASYNC_EXECUTOR_NAME)
	public void count(String userName) {
		int userCount = countMap.get(userName) == null ? 0:countMap.get(userName);
		synchronized (countMap) {
			countMap.put(userName, userCount + 1);
		}
	}
	@PostConstruct
	public void sendMqs() throws InterruptedException {
		while(true) {
			Thread.sleep(6000);
			// 发送count
			//this.amqpTemplate.convertAndSend(routingKey, object, correlationData);
			countMap = new HashMap<>();
		}
	}
}

