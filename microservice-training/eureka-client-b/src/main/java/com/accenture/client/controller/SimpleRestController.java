package com.accenture.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.client.dao.IClassBDao;

@RestController
@RequestMapping("/api/clientb")
public class SimpleRestController {
private static final Logger LOG = LoggerFactory.getLogger(SimpleRestController.class);

	@Value("${spring.hostname}")
	private String hostName;
	
	@Autowired
	private IClassBDao iClassDao;
	
	@RequestMapping(value ="/classInfo",method = RequestMethod.GET)
	public String getClassInfo(@RequestParam("id") String id) {
		return hostName + ":" + iClassDao.getClassName(id);
	}
}
