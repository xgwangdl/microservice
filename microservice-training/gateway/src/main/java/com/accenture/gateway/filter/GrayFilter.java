package com.accenture.gateway.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.accenture.common.auth.Sign;
import com.accenture.common.mq.RabbitMQConfig;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;

@Component
@RefreshScope
public class GrayFilter extends ZuulFilter {

	@Autowired
	private RabbitTemplate amqpTemplate;

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Value("${auth.token.secret}")
	private String secret;

	@Value("${server.port}")
	private String port;

	private static final Logger LOG = LoggerFactory.getLogger(GrayFilter.class);

	@Override
	public Object run() throws ZuulException {

		LOG.info("gateway port is:" + this.port);

		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String authorization = request.getHeader("Authorization");

		// check token
		DecodedJWT decodedJWT = null;
		try {
			decodedJWT = Sign.verifyToken(authorization, secret);
		} catch (Exception ex) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			ctx.setResponseBody(ex.getMessage());
			return null;
		}

		String userName = decodedJWT.getClaim("userName").asString();
		ctx.addZuulRequestHeader("userName", userName);

		Map<String, String> loginMap = new HashMap<>();
		loginMap.put("userName", userName);
		loginMap.put("gateWayPort", this.port);

		// check is gray
		if ("admin".equals(userName)) {
			RibbonFilterContextHolder.getCurrentContext().add("host-mark", "gray");
			loginMap.put("environment", "gray");
		} else {
			RibbonFilterContextHolder.getCurrentContext().add("host-mark", "release");
			loginMap.put("environment", "release");
		}

		this.amqpTemplate.convertAndSend(RabbitMQConfig.GATE_WAY_EXCHANGE, RabbitMQConfig.GATE_WAY_POINT_KEY, loginMap);

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
