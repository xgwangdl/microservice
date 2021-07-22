package com.accenture.client.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.accenture.common.util.result.CommonResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderFallback implements IOrderInterface {

	@Override
	public CommonResult<List<Map<String, Object>>> getOrderInfo(String authz, Integer userId) {
		log.info("saveOrderInfo降级");
		return CommonResult.failed();
	}

	@Override
	public CommonResult<Map<String, Object>> saveOrderInfo(String authz, Integer orderId) {
		log.info("saveOrderInfo降级");
		return CommonResult.failed();
	}

	
}
