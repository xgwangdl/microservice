package com.accenture.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.client.dao.IClassBDao;
import com.accenture.common.auth.Authz;
import com.accenture.common.util.ApplicationContext;

@RestController
@RequestMapping("/api/clientb")
public class SimpleRestController {
	
	@Autowired
	private IClassBDao iClassDao;
	
	@Authz(value= {"aService","bService"})
	@RequestMapping(value ="/classInfo",method = RequestMethod.GET)
	public String getClassInfo(@RequestParam("id") String id) throws InterruptedException {
		return ApplicationContext.getUserId() + iClassDao.getClassName(id);
	}
	
	@Authz(value= {"aService","bService"})
	@RequestMapping(value ="/classInfoTest",method = RequestMethod.GET)
	public String getClassInfoTest(@RequestParam("id") String id) throws InterruptedException {
		return ApplicationContext.getUserId() + id;
	}
}
