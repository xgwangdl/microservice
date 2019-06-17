package com.accenture.gateway.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.jmnarloch.spring.cloud.ribbon.api.RibbonFilterContext;
import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;

import com.accenture.common.util.*;

public class GrayFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		String jsonStr = request.getHeader("info");
		try {
			Map<String,Object> infoMap = JSONUtils.json2map(jsonStr);
			// check token
			
			// check is gray
			if ("admin".equals((String)infoMap.get("userName"))) {
				RibbonFilterContextHolder.getCurrentContext().add("host-mark", "gray");
			} else {
				RibbonFilterContextHolder.getCurrentContext().add("host-mark", "release");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

}
