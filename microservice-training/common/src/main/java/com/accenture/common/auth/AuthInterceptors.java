package com.accenture.common.auth;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.accenture.common.exception.RestException;
import com.accenture.common.util.ApplicationContext;

public class AuthInterceptors extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Authz authz = handlerMethod.getMethodAnnotation(Authz.class);
			if (authz == null) {
				return true;
			}
			
			String value = ApplicationContext.getAuthz();
			if (StringUtils.isEmpty(value)) {
				throw new RestException("301","没有Authz！");
			}
			String[] listValue = authz.value();
			if (!Arrays.asList(listValue).contains(value)) {
				throw new RestException("302","Authz不合法！");
			}
		}
		return true;
	}
}
