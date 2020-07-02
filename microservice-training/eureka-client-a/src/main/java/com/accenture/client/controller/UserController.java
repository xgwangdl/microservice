package com.accenture.client.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.accenture.client.dao.IUserADao;
import com.accenture.client.service.AggregatedService;
import com.accenture.common.auth.Sign;
import com.accenture.common.exception.RestException;
import com.accenture.common.util.ApplicationContext;
import com.accenture.common.util.result.CommonResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RefreshScope
@EnableCircuitBreaker
@RequestMapping("/api/user")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IOrderInterface orderInterface;

	@Autowired
	private IUserADao iUserAdao;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AggregatedService countService;

	@Value("${spring.hostname}")
	private String hostName;
	
	/**
	 * 使用feignclient调用
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/showLoginUserInfo", method = RequestMethod.GET)
	public CommonResult<Map<String, Object>> showLoginUserInfo() {
		String userName = ApplicationContext.getUserName();
		Map<String, Object> userInfo = iUserAdao.getUser(userName);
		LOG.info("user info : " + userInfo);
		String authz = ApplicationContext.getAuthz();
		CommonResult<List<Map<String, Object>>> orderInfo = orderInterface.getOrderInfo(authz,((Long) userInfo.get("ID")).intValue());
		if (orderInfo.getCode() == 200L) {
			List<Map<String, Object>> orderInfos = orderInfo.getData();
			userInfo.put("Orders", orderInfos);
		} else {
			throw new RestException("201", orderInfo.getMessage());
		}
		
		this.countService.count((Long) userInfo.get("ID"));
		
		return CommonResult.success(userInfo);
	}

	@RequestMapping(value = "/showLoginCount", method = RequestMethod.GET)
	public CommonResult<Map<String, Object>> showLoginCount() {
		String userName = ApplicationContext.getUserName();
		Map<String, Object> userInfo = iUserAdao.getUser(userName);
		LOG.info("user info : " + userInfo);
		Map<String,Object> aggregatedMap = this.iUserAdao.getAggregated(((Long) userInfo.get("ID")).intValue());
		return CommonResult.success(aggregatedMap);
	}
//	/**
//	 * 使用feignclient调用
//	 * 
//	 * @param name
//	 * @return
//	 */
//	@RequestMapping(value = "/hellowordTest", method = RequestMethod.GET)
//	@Authz(value= {"aService"})
//	public CommonResult<Map<String, Object>> hellowordTest(@RequestParam String name) {
//		Map<String,Object> userInfo = new HashMap<>();
//		String authz = ApplicationContext.getAuthz();
//		String userId = ApplicationContext.getUserId();
//		String className = clientb.getClassInfoTest(authz,"test");
////		ResponseEntity<String> responseEntity = restTemplate.getForEntity(
////				"http://CLIENT-B//api/clientb/classInfoTest?id=" + userId, String.class);
//		
//		userInfo.put("test", hostName + ":" + className);
//		
//		return CommonResult.success(userInfo);
//	}
//	
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
	
	/**
	 * 获取Token。
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getToken/{userName}/{secret}", method = RequestMethod.GET)
	public String getToken(@PathVariable String userName,@PathVariable String secret) {
		return Sign.getToken(userName, secret);
	}
}
