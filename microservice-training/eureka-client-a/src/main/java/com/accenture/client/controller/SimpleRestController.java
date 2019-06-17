package com.accenture.client.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.client.dao.IUserADao;
import com.accenture.common.util.result.CommonResult;

@RestController
@RequestMapping("/api/clienta")
public class SimpleRestController {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleRestController.class);
	
	@Autowired
	private IClientBInterface clientb;
	
	@Autowired
	private IUserADao iUserAdao;
	
	@RequestMapping(value ="/helloword",method = RequestMethod.GET)
	public CommonResult<Map<String,Object>> helloword(@RequestParam String name) {
		Map<String,Object> userInfo = iUserAdao.getUser(name);
		LOG.info("user info : " + userInfo);
		String className = clientb.getClassInfo((String)userInfo.get("classId"));
		userInfo.put("class", className);
		return CommonResult.success(userInfo);
	}
}
