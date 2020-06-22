package com.accenture.common.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accenture.common.util.result.CommonResult;

@ControllerAdvice
public class RestExceptionAdvice {

	@ResponseBody
	@ExceptionHandler(value=RestException.class)
	public CommonResult<Map<String, Object>> restException(RestException e,HttpServletRequest request) {
		return CommonResult.failed(e.getMessage());
	}
}
