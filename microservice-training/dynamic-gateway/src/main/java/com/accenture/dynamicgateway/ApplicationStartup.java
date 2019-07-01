package com.accenture.dynamicgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.accenture.dynamicgateway.service.GatewayDefineService;


@Component
public class ApplicationStartup implements ApplicationRunner {

	@Autowired
    private GatewayDefineService gatewayDefineService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		gatewayDefineService.loadRouteDefinition();		
	}

}
