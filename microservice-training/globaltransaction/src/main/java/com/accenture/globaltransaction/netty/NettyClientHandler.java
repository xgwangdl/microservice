package com.accenture.globaltransaction.netty;

import org.apache.commons.lang.StringUtils;

import com.accenture.globaltransaction.MyTransaction;
import com.accenture.globaltransaction.TransactionManager;
import com.accenture.globaltransaction.TransactionType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

	private ChannelHandlerContext ctx;

	public void channel(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("客户端Active .....");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg == null) {
    		return;
    	}
		String jsonStr = null;
		if (msg instanceof String) {
			jsonStr = (String)msg;
		} else {
			ByteBuf byteBuf = (ByteBuf) msg;
	    	jsonStr = byteBuf.toString(CharsetUtil.UTF_8);
	    	
		}
		if (StringUtils.isEmpty(jsonStr)) {
			return;
		}
		
		log.info("receive data:" + jsonStr);
		JSONObject jsonObject = JSON.parseObject(jsonStr);
		

		String groupId = jsonObject.getString("groupId");
		String transactionType = jsonObject.getString("transactionType");

		MyTransaction myTransaction = TransactionManager.getMyTransaction(groupId);

		if ("commit".equals(transactionType)) {
			myTransaction.setTransactionType(TransactionType.commit);
		} else {
			myTransaction.setTransactionType(TransactionType.rollback);
		}
		myTransaction.getTask().signalTask();
		log.info("线程解锁继续");
	}
}
