package com.accenture.globaltransaction.aspect;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.accenture.globaltransaction.MyConnection;
import com.accenture.globaltransaction.MyTransactionContext;


@Aspect
@Component
public class ConnectionAspect {
	@Around(value = "execution(* javax.sql.DataSource.getConnection(..))")
	public Connection getConnection(ProceedingJoinPoint point) throws Throwable {
		Connection connection = (Connection) point.proceed();
		MyConnection myConnection = new MyConnection(connection);
		Map<String,Object> threadMap = new HashMap<>();
		threadMap.put("myConnection", myConnection);
		MyTransactionContext.setMyTransaction(threadMap);
		
		return myConnection;
	}
}
