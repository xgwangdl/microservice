package com.accenture.globaltransaction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.accenture.globaltransaction.netty.NettyClient;
import com.alibaba.fastjson.JSONObject;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionManager {

	private static Map<String,MyTransaction> myTransactionMap = new HashMap<>();
	
	public static String createGroup() throws Exception {
		return UUID.randomUUID().toString();
	}
	
	public static MyTransaction createMyTransaction(String groupId) {
		String transactionId = UUID.randomUUID().toString();
		return new MyTransaction(groupId,transactionId);
	}
	
	public static void addMyTransaction(MyTransaction myTransaction) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("groupId", myTransaction.getGroupId());
		jsonObject.put("transactionId", myTransaction.getTransactionId());
		jsonObject.put("transactionType", myTransaction.getTransactionType());
		jsonObject.put("isCompelete", myTransaction.isCompelete());
		jsonObject.put("command", "regist");
		
		myTransactionMap.put(myTransaction.getGroupId(), myTransaction);
		System.out.print("regist transaction");
		log.info("发送数据:" + jsonObject.toJSONString());
		NettyClient client = new NettyClient("127.0.0.1", 6001);
		client.start();
		Channel channel = client.getChannel();
		channel.writeAndFlush(jsonObject.toJSONString());
	}
	
	public static MyTransaction getMyTransaction(String groupId) {
		return myTransactionMap.get(groupId);
	}
}
