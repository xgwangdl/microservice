package com.accenture.client.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.accenture.client.config.AppConfig;
import com.accenture.common.mq.RabbitMQConfig;

@Service
public class AggregatedService {

	@Autowired
	private RabbitTemplate amqpTemplate;
	
	private static Map<Long,List<Timestamp>> countMap = new HashMap<>();
	
	@Async(AppConfig.ASYNC_EXECUTOR_NAME)
	public void count(Long userId) {
		synchronized (countMap) {
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			List<Timestamp> loginTime = countMap.get(userId);
			if (loginTime == null) {
				loginTime = new ArrayList<>();
			}
			loginTime.add(ts);
			countMap.put(userId, loginTime);
		}
	}
	
	@PostConstruct
	public void sendMqs() throws InterruptedException {
		RabbitTemplate rt = this.amqpTemplate;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (countMap.size() > 0) {
						rt.convertAndSend(RabbitMQConfig.COUNT_EXCHANGE, RabbitMQConfig.COUNT_POINT_KEY, countMap);
					}
					countMap = new HashMap<>();
				}
			}
		}).start();
	}
}

