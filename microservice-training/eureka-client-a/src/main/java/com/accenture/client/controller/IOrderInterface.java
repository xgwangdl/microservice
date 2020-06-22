package com.accenture.client.controller;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.accenture.common.util.ApplicationContext;
import com.accenture.common.util.result.CommonResult;

@FeignClient(name = "client-b",path = "/api/order", fallback= OrderFallback.class)
public interface IOrderInterface {
	@RequestMapping(value = "/getOrderInfo", method = RequestMethod.GET)
	public CommonResult<List<Map<String, Object>>> getOrderInfo(@RequestHeader(ApplicationContext.AUTH_Z) String authz,
			@RequestParam("userId") Integer userId);
}
