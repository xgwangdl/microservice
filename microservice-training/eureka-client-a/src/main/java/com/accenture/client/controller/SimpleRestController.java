package com.accenture.client.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.accenture.client.dao.IUserADao;
import com.accenture.common.util.result.CommonResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/api/clienta")
public class SimpleRestController {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleRestController.class);

	@Autowired
	private IClientBInterface clientb;

	@Autowired
	private IUserADao iUserAdao;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AmqpTemplate amqpTemplate;
	
	/**
	 * 使用feignclient调用
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/helloword", method = RequestMethod.GET)
	public CommonResult<Map<String, Object>> helloword(@RequestParam String name) {
		Map<String, Object> userInfo = iUserAdao.getUser(name);
		LOG.info("user info : " + userInfo);
		String className = clientb.getClassInfo((String) userInfo.get("classid"));
		userInfo.put("class", className);
		
		this.amqpTemplate.convertAndSend("microservice.clienta", "microservice.clienta-key", userInfo);
		
		return CommonResult.success(userInfo);
	}

	/**
	 * 使用resttemplate調用
	 * @param name
	 * @return
	 */
	@HystrixCommand(fallbackMethod="ClientBFallback.class")
	@RequestMapping(value = "/loadbalance", method = RequestMethod.GET)
	public CommonResult<Map<String, Object>> loadbalance(@RequestParam String name) {
		Map<String, Object> userInfo = iUserAdao.getUser(name);
		LOG.info("user info : " + userInfo);
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(
				"http://CLIENT-B//api/clientb/classInfo?id=" + (String) userInfo.get("classid"), String.class);
		userInfo.put("class", responseEntity.getBody());
		return CommonResult.success(userInfo);
	}
}
