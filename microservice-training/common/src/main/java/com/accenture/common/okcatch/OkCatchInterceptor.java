package com.accenture.common.okcatch;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OkCatchInterceptor {

	@Autowired
	private OkCatch okCatch;
	
	@Pointcut("@annotation(com.accenture.common.okcatch.Catch)")
	public void annotationPointCut() {}

	@Around("execution(*com.accenture.client.dao.*(..))")
	public Object around(ProceedingJoinPoint  joinPoint) throws Throwable {
		String key = this.getCatchKey(joinPoint);
		Object result = this.okCatch.get(key);
		if (result == null) {
			result = joinPoint.proceed();
			this.okCatch.put(key, result);
		}
		return result;
	}
	
	private String getCatchKey(ProceedingJoinPoint  joinPoint) {
		Class targetClass = joinPoint.getTarget().getClass();
		Object[] arguments = joinPoint.getArgs();
		String methodName = joinPoint.getSignature().getName();
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					return method.getAnnotation(Catch.class).key();
				}
			}
		}
		return null;
	}
}
