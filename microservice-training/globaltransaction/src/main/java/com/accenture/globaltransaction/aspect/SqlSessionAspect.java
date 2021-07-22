package com.accenture.globaltransaction.aspect;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.accenture.globaltransaction.MyTransaction;
import com.accenture.globaltransaction.MyTransactionContext;
import com.accenture.globaltransaction.TransactionType;

@Aspect
@Component
public class SqlSessionAspect {
	@Around(value = "execution(* com.alibaba.druid.pool.DruidPooledConnection.commit(..))")
	public void commit(ProceedingJoinPoint point) throws Throwable {
		Map<String,Object> threadLocalMap = MyTransactionContext.getMyTransaction();
		if (threadLocalMap != null && threadLocalMap.get("myTransaction") != null) {
			MyTransaction myTransaction = (MyTransaction)threadLocalMap.get("myTransaction");
			new Thread() {
				@Override
			    public void run() {
					System.out.println("TransactionId:" + myTransaction.getTransactionId());
					myTransaction.getTask().waitTask();
					System.out.println("commit:" + myTransaction.getTransactionType());
					if (myTransaction.getTransactionType().equals(TransactionType.commit)) {
						try {
							point.proceed();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			    }
			}.start();
		} else {
			point.proceed();
		}
	}
}
