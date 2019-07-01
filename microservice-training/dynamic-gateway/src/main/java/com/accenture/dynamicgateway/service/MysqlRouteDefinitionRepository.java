package com.accenture.dynamicgateway.service;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;

import com.accenture.common.util.JSONUtils;
import com.accenture.dynamicgateway.dto.GetwayConfigDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository {

	@Autowired
	private GatewayDefineService gatewayDefineService;

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		try {
			List<GetwayConfigDTO> gatewayDefineList = gatewayDefineService.findAll();
			Map<String, RouteDefinition> routes = new LinkedHashMap<String, RouteDefinition>();
			for (GetwayConfigDTO gatewayDefine : gatewayDefineList) {
				RouteDefinition definition = new RouteDefinition();
				definition.setId(gatewayDefine.getId());
				definition.setUri(new URI(gatewayDefine.getUri()));
				List<PredicateDefinition> predicateDefinitions = gatewayDefine.getPredicateDefinition();
				if (predicateDefinitions != null) {
					definition.setPredicates(predicateDefinitions);
				}
				List<FilterDefinition> filterDefinitions = gatewayDefine.getFilterDefinition();
				if (filterDefinitions != null) {
					definition.setFilters(filterDefinitions);
				}
				routes.put(definition.getId(), definition);

			}
			return Flux.fromIterable(routes.values());
		} catch (Exception e) {
			e.printStackTrace();
			return Flux.empty();
		}
	}

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return route.flatMap(r -> {
			GetwayConfigDTO gatewayDefine = new GetwayConfigDTO();
			gatewayDefine.setId(r.getId());
			gatewayDefine.setUri(r.getUri().toString());
			try {
				gatewayDefine.setPredicates(JSONUtils.obj2json(r.getPredicates()));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				gatewayDefine.setFilters(JSONUtils.obj2json(r.getFilters()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				gatewayDefineService.save(gatewayDefine);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Mono.empty();
		});
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return routeId.flatMap(id -> {
			try {
				gatewayDefineService.deleteById(id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Mono.empty();
		});
	}

}
