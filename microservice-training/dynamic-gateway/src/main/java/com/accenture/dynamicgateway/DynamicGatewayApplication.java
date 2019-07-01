package com.accenture.dynamicgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.accenture.dynamicgateway.service.MysqlRouteDefinitionRepository;

@EnableEurekaClient
@SpringBootApplication
public class DynamicGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicGatewayApplication.class, args);
	}

	@Bean
    public RouteDefinitionWriter routeDefinitionWriter() {
        return new InMemoryRouteDefinitionRepository();
    }
	
	 @Bean
    public MysqlRouteDefinitionRepository mysqlRouteDefinitionRepository() {
        return new MysqlRouteDefinitionRepository();
    }
}
