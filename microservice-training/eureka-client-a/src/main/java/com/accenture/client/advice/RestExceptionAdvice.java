package com.accenture.client.advice;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accenture.common.exception.RestException;
import com.accenture.common.util.result.CommonResult;
import com.netflix.hystrix.exception.HystrixRuntimeException;

@ControllerAdvice
public class RestExceptionAdvice {

	@ResponseBody
	@ExceptionHandler(value=HystrixRuntimeException.class)
	public CommonResult<Map<String, Object>> restException(HystrixRuntimeException e,HttpServletRequest request) {
		if (e.getFallbackException().getCause().getCause() instanceof RestException) {
			RestException restException = (RestException)e.getFallbackException().getCause().getCause();
			return CommonResult.failed(restException.getMessage());
		}
		throw e;
	}
}
