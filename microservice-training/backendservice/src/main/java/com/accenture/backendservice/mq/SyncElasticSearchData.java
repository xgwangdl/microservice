package com.accenture.backendservice.mq;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.accenture.backendservice.dto.UserInfo;
import com.accenture.backendservice.elasticsearch.UserRepository;

@Component
@RabbitListener(queues = "sendData")
public class SyncElasticSearchData {
	private static final Logger LOG = LoggerFactory.getLogger(SyncElasticSearchData.class);
	
	@Autowired
	private UserRepository userRepostory;
	
	@RabbitHandler
    public void process(Map<String, Object> userInfo) {
		LOG.info("Receiver1  : " + userInfo);
		UserInfo user = new UserInfo();
		user.setId((Long)userInfo.get("id"));
		user.setUserName((String)userInfo.get("name"));
		user.setClassname((String)userInfo.get("class"));
		userRepostory.save(user);
    }
}
