package com.accenture.globaltransaction;

import java.util.Map;

public class MyTransactionContext {
	private static ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<>();

	public static Map<String, Object> getMyTransaction() {
		return threadLocal.get();
	}

	public static void setMyTransaction(Map<String, Object> myTransaction) {
		MyTransactionContext.threadLocal.set(myTransaction);
	}
	
	
}
