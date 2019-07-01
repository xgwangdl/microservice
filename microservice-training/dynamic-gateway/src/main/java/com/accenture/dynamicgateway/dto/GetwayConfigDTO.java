package com.accenture.dynamicgateway.dto;

import java.util.List;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import com.accenture.common.util.JSONUtils;

public class GetwayConfigDTO {

	private String id;

	private String uri;

	private String predicates;

	private String filters;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getPredicates() {
		return predicates;
	}

	public void setPredicates(String predicates) {
		this.predicates = predicates;
	}

    public List<PredicateDefinition> getPredicateDefinition() throws Exception {
        if (this.predicates != null) {
            List<PredicateDefinition> predicateDefinitionList = JSONUtils.json2list(this.predicates, PredicateDefinition.class);
            return predicateDefinitionList;
        } else {
            return null;
        }
    }
    
	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}
	
    public List<FilterDefinition> getFilterDefinition() throws Exception {
        if (this.filters != null) {
            List<FilterDefinition> filterDefinitionList = JSONUtils.json2list(this.filters, FilterDefinition.class);
            return filterDefinitionList;
        } else {
            return null;
        }
    }
}
