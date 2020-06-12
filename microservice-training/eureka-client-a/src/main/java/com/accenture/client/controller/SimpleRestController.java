package com.accenture.client.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.accenture.client.dao.IUserADao;
import com.accenture.common.auth.Authz;
import com.accenture.common.util.ApplicationContext;
import com.accenture.common.util.result.CommonResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RefreshScope
@EnableCircuitBreaker
@RequestMapping("/api/clienta")
public class SimpleRestController {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleRestController.class);

	@Autowired
	private IClientBInterface clientb;

	@Autowired
	private IUserADao iUserAdao;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${spring.hostname}")
	private String hostName;
	
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
		String authz = ApplicationContext.getAuthz();
		String className = clientb.getClassInfo(authz,(String) userInfo.get("classid"));
		userInfo.put("class", hostName + ":" + className);
		
		return CommonResult.success(userInfo);
	}

	/**
	 * 使用feignclient调用
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/hellowordTest", method = RequestMethod.GET)
	@Authz(value= {"aService"})
	public CommonResult<Map<String, Object>> hellowordTest(@RequestParam String name) {
		Map<String,Object> userInfo = new HashMap<>();
		String authz = ApplicationContext.getAuthz();
		String userId = ApplicationContext.getUserId();
		String className = clientb.getClassInfoTest(authz,"test");
//		ResponseEntity<String> responseEntity = restTemplate.getForEntity(
//				"http://CLIENT-B//api/clientb/classInfoTest?id=" + userId, String.class);
		
		userInfo.put("test", hostName + ":" + className);
		
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
