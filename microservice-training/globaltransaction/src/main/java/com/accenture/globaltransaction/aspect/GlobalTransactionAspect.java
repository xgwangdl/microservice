package com.accenture.globaltransaction.aspect;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.accenture.globaltransaction.MyConnection;
import com.accenture.globaltransaction.MyTransaction;
import com.accenture.globaltransaction.MyTransactionContext;
import com.accenture.globaltransaction.TransactionManager;
import com.accenture.globaltransaction.TransactionType;
import com.accenture.globaltransaction.anotaion.GlobalTransaction;
import com.accenture.globaltransaction.anotaion.Transaction;

@Aspect
@Component
public class GlobalTransactionAspect {
	@Around(value = "@annotation(globalTransaction)")
	public Object startMyTransaction(ProceedingJoinPoint point, GlobalTransaction globalTransaction) throws Throwable {
		Map<String, Object> threadMap = MyTransactionContext.getMyTransaction();
		// make sid
		String groupId = TransactionManager.createGroup();
		threadMap.put("groupId", groupId);
		this.setAttributes(groupId);

		return doGlobalTransactional(point, threadMap, groupId, true);
	}

	private Object doGlobalTransactional(ProceedingJoinPoint point, Map<String, Object> threadMap, String groupId,
			boolean isCompelete) throws Exception, Throwable {
		Object value = null;
		MyTransaction myTransaction = TransactionManager.createMyTransaction(groupId);
		threadMap.put("myTransaction", myTransaction);
		MyTransactionContext.setMyTransaction(threadMap);

		MyConnection myConnection = (MyConnection) threadMap.get("myConnection");
		myConnection.setMyTransaction(myTransaction);
		try {
			value = point.proceed();
			myTransaction.setTransactionType(TransactionType.commit);
		} catch (Throwable e) {
			myTransaction.setTransactionType(TransactionType.rollback);
			myTransaction.setCompelete(isCompelete);
			TransactionManager.addMyTransaction(myTransaction);
			throw e;
		}
		myTransaction.setCompelete(isCompelete);
		TransactionManager.addMyTransaction(myTransaction);
		return value;
	}

	@Around(value = "@annotation(transaction)")
	public Object startTransaction(ProceedingJoinPoint point, Transaction transaction) throws Throwable {
		String groupId = this.getRequestHeader("groupId");
		if (StringUtils.isNotEmpty(groupId)) {
			Map<String, Object> threadMap = MyTransactionContext.getMyTransaction();
			return doGlobalTransactional(point, threadMap, groupId, false);
		} else {
			return point.proceed();
		}
	}

	private void setAttributes(String key) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			request.setAttribute("groupId", key);
		}
	}

	private String getRequestHeader(String key) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			return request.getHeader(key);
		}
		return null;
	}
}
