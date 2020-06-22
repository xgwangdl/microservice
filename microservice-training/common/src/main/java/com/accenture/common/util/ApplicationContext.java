package com.accenture.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ApplicationContext {

	public static final String USER_NAME = "userName";
	public static final String AUTH_Z = "authz";
	
	private static String getRequestHeader(String key) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			return request.getHeader(key);
		}
		return null;
	}
	
	public static String getUserName() {
		return getRequestHeader(USER_NAME);
	}
	
	public static String getAuthz() {
		return getRequestHeader(AUTH_Z);
	}
}

	