package com.accenture.client.controller;

import org.springframework.stereotype.Component;

import com.accenture.common.exception.RestException;

@Component
public class ClientBFallback implements IClientBInterface {

	@Override
	public String getClassInfo(String id) {
		throw new RestException("202","糟糕，服务client-b找不到了！");
	}
	
}
