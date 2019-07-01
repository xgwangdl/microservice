package com.accenture.dynamicgateway.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.accenture.dynamicgateway.dao.IGatewayConfig;
import com.accenture.dynamicgateway.dto.GetwayConfigDTO;

import reactor.core.publisher.Mono;

@Service
public class GatewayDefineService implements ApplicationEventPublisherAware {
	@Autowired
	IGatewayConfig iGatewayConfig;
	
	@Autowired
    private RouteDefinitionWriter routeDefinitionWriter;
	
	private ApplicationEventPublisher publisher;
	
	public List<GetwayConfigDTO> findAll() {
		return iGatewayConfig.findAll();
	}
	
    public String loadRouteDefinition() {
        try {
            List<GetwayConfigDTO> gatewayDefineServiceAll = iGatewayConfig.findAll();
            if (gatewayDefineServiceAll == null) {
                return "none route defined";
            }
            for (GetwayConfigDTO getwayConfigDto : gatewayDefineServiceAll) {
                RouteDefinition definition = new RouteDefinition();
                definition.setId(getwayConfigDto.getId());
                definition.setUri(new URI(getwayConfigDto.getUri()));
                List<PredicateDefinition> predicateDefinitions = getwayConfigDto.getPredicateDefinition();
                if (predicateDefinitions != null) {
                    definition.setPredicates(predicateDefinitions);
                }
                List<FilterDefinition> filterDefinitions = getwayConfigDto.getFilterDefinition();
                if (filterDefinitions != null) {
                    definition.setFilters(filterDefinitions);
                }
                routeDefinitionWriter.save(Mono.just(definition)).subscribe();
                this.publisher.publishEvent(new RefreshRoutesEvent(this));
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }
    
    public GetwayConfigDTO save(GetwayConfigDTO gatewayDefine) throws Exception {
    	iGatewayConfig.save(gatewayDefine);
        return gatewayDefine;
    }
 
    public void deleteById(String id) throws Exception {
    	iGatewayConfig.deleteById(id);
    }

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
 
}
