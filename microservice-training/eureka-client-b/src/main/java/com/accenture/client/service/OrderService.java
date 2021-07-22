package com.accenture.client.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.client.dao.IOrderDao;
import com.accenture.common.mq.RabbitMQConfig;

@Service
public class OrderService {
	@Autowired
	private RabbitTemplate amqpTemplate;
	
	@Autowired
	private IOrderDao iOrderDao;
	
	@PostConstruct
	public void sendOrderInfo() {
		RabbitTemplate rt = this.amqpTemplate;
		IOrderDao iOrderDao = this.iOrderDao;
		new Thread(new Runnable() {
					@Override
					public void run() {
						while(true) {
							try {
								Thread.sleep(60000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							List<Map<String,Object>> outBoxInfo = iOrderDao.getOutboxInfoByNotProcess();
							outBoxInfo.forEach(e -> {
								Map<String,String> content = new HashMap<>();
								content.put("orderType", (String)e.get("ORDER_TYPE"));
								content.put("content", (String)e.get("CONTENT"));
								rt.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE, RabbitMQConfig.ORDER_POINT_KEY, content);
								iOrderDao.updateOutboxInfo(((Long)e.get("ID")).intValue(), "3");
							});
						}
					}
		}).start();
	}
}
