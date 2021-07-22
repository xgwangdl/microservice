package com.accenture.client.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.client.dao.IUserADao;
import com.accenture.common.util.JSONUtils;

@Service
@RabbitListener(queues = "orderData")
public class PointService {
	
	@Autowired
	private IUserADao iUserAdao;
	
	@RabbitHandler
    public void process(Map<String,String> msg) throws Exception {
		String orderType = msg.get("orderType");
		int unit = 1;
		if ("2".equals(orderType)) {
			unit = -1;
		}
		String content = msg.get("content");
		Map<String,Object> orderData = JSONUtils.json2map(content);
		if (orderData != null && (Integer)orderData.get("userId") != null) {
			Integer userId = (Integer)orderData.get("userId");
			Integer price = (Integer)orderData.get("price");
			Integer count = (Integer)orderData.get("count");
			Integer money = price * count * unit;
			Timestamp orderTime = (Timestamp)orderData.get("order_time");
			this.iUserAdao.updateUser(userId, money);
			this.iUserAdao.savePoint(userId, money, orderTime);
		}
	}
}
