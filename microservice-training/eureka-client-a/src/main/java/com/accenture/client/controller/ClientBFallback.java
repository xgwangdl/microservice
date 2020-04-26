package com.accenture.client.controller;

import org.springframework.stereotype.Component;

import com.accenture.common.exception.RestException;

@Component
public class ClientBFallback implements IClientBInterface {

	@Override
	public String getClassInfo(String authz, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClassInfoTest(String authz, String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
