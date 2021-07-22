package com.accenture.common.auth;

import org.apache.commons.lang.StringUtils;

import com.accenture.common.util.ApplicationContext;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignReqeustHandler implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		String userName = ApplicationContext.getUserName();
		template.header(ApplicationContext.USER_NAME, userName);
		
		String groupId = ApplicationContext.getAttributes("groupId");
		if (StringUtils.isNotEmpty(groupId)) {
			template.header(ApplicationContext.GROUP_ID, groupId);
		}	
	}

}
