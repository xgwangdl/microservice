package com.accenture.client.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.accenture.common.util.ApplicationContext;

@FeignClient(name="client-b")
public interface IClientBInterface {
	@RequestMapping(value ="/api/clientb/classInfo",method = RequestMethod.GET)
	public String getClassInfo(@RequestHeader(ApplicationContext.AUTH_Z) String authz, @RequestParam("id") String id);
	
	@RequestMapping(value ="/api/clientb/classInfoTest",method = RequestMethod.GET)
	public String getClassInfoTest(@RequestHeader(ApplicationContext.AUTH_Z) String authz, @RequestParam("id") String id);
}
