package com.accenture.gateway.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.HttpHead;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.accenture.common.util.JSONUtils;
import com.accenture.common.util.result.CommonResult;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;
import io.reactivex.netty.protocol.http.client.HttpRequestHeaders;

@Component
@RefreshScope
public class GrayFilter extends ZuulFilter {

	@Value("${gateway.token}")
	private String token;
	
	@Autowired
	private RabbitTemplate amqpTemplate;
	
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String jsonStr = request.getHeader("info");
		String authorization = request.getHeader("Authorization");
		try {
			Map<String,Object> infoMap = JSONUtils.json2map(jsonStr);
			// check token
			if (!token.equals(authorization)) {
				ctx.setSendZuulResponse(false);
	            ctx.setResponseStatusCode(401);
	            ctx.setResponseBody(JSONUtils.obj2json(CommonResult.failed("token is error")));
			}
			// check is gray
			if ("admin".equals((String)infoMap.get("userName"))) {
				RibbonFilterContextHolder.getCurrentContext().add("host-mark", "gray");
			} else {
				RibbonFilterContextHolder.getCurrentContext().add("host-mark", "release");
			}
			this.amqpTemplate.convertAndSend("microservice.clienta", "microservice.clienta-key", infoMap);
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
