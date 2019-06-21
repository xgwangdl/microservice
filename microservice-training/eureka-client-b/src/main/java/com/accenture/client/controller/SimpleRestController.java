package com.accenture.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.client.dao.IClassBDao;

@RestController
@RefreshScope
@RequestMapping("/api/clientb")
public class SimpleRestController {
	
	@Autowired
	private IClassBDao iClassDao;
	
	@RequestMapping(value ="/classInfo",method = RequestMethod.GET)
	public String getClassInfo(@RequestParam("id") String id) throws InterruptedException {
		return iClassDao.getClassName(id);
	}
}
