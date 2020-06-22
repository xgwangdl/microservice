package com.accenture.client.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.accenture.common.util.result.CommonResult;

@Component
public class OrderFallback implements IOrderInterface {

	@Override
	public CommonResult<List<Map<String, Object>>> getOrderInfo(String authz, Integer userId) {
		return CommonResult.failed();
	}

	
}
