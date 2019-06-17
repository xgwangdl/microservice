package com.accenture.client.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="client-b")
public interface IClientBInterface {
	@RequestMapping(value ="/api/clientb/classInfo",method = RequestMethod.GET)
	public String getClassInfo(@RequestParam String id);
}
