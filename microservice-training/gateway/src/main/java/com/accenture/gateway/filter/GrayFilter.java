package com.accenture.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import com.accenture.common.auth.Sign;
import com.accenture.common.config.WebInterceptorConfig;
import com.accenture.common.exception.RestException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;

@Import(value= {WebInterceptorConfig.class})
@Component
@RefreshScope
public class GrayFilter extends ZuulFilter {

	@Autowired
	private RabbitTemplate amqpTemplate;
	
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Value("${auth.token}")
	private String secret;
	
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String authorization = request.getHeader("Authorization");
		
		// check token
		DecodedJWT decodedJWT = null;
		try {
			decodedJWT = Sign.verifyToken(authorization, secret);
		} catch(RestException ex) {
			ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody(ex.getMessage());
		}
		
		String userId = decodedJWT.getClaim("userId").asString();
		ctx.addZuulRequestHeader("userId", userId);
		
		
		// check is gray
		if ("admin".equals(userId)) {
			RibbonFilterContextHolder.getCurrentContext().add("host-mark", "gray");
		} else {
			RibbonFilterContextHolder.getCurrentContext().add("host-mark", "release");
		}
		this.amqpTemplate.convertAndSend("microservice.clienta", "microservice.clienta-key", userId);
		
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
