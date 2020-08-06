package com.accenture.backendservice.mq;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.accenture.backendservice.dao.IAggregatedDao;
import com.accenture.backendservice.dto.AggregatedDto;

@Component
@RabbitListener(queues = "countData")
public class SyncAggregated {
	private static final Logger LOG = LoggerFactory.getLogger(SyncAggregated.class);
	
	@Autowired
	private IAggregatedDao dao;
	
	@RabbitHandler
    public void process(Map<Long,List<Timestamp>> countMap) {
		LOG.info("Receiver1  : " + countMap);
		countMap.forEach((key,value) -> {
			AggregatedDto dto = new AggregatedDto();
			dto.setUserId(key.intValue());
			dto.setCount(value.size());
			dto.setLoginTimes(value);
			this.dao.mergeAggregated(dto);
		});
    }
}
