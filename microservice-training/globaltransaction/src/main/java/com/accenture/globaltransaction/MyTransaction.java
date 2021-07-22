package com.accenture.globaltransaction;

public class MyTransaction {

	private String transactionId;
	private TransactionType transactionType;
	private String groupId;
	private boolean isCompelete;
	private Task task;
	
	public MyTransaction(String groupId,String transactionId) {
		this.transactionId = transactionId;
		this.groupId = groupId;
		this.task = new Task();
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public boolean isCompelete() {
		return isCompelete;
	}

	public void setCompelete(boolean isCompelete) {
		this.isCompelete = isCompelete;
	}
	
	
}
