package org.myseata.netty;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	
	private final static Map<String, Map<String,ChannelHandlerContext>> groupThreadMap = new HashMap<>();
	
    /**
     * 客户端连接会触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	System.out.println("服务端接收数据完毕..");
        //ctx.flush();
    }

    /**
     * 客户端发消息会触发
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	if (msg == null) {
    		return;
    	}
    	ByteBuf byteBuf = (ByteBuf) msg;
    	String jsonStr = byteBuf.toString(CharsetUtil.UTF_8);
    	log.info("接受的消息：" + jsonStr);
    	JSONObject jsonObject = JSON.parseObject(jsonStr);
    	String groupId = jsonObject.getString("groupId");
		String command = jsonObject.getString("command");
		String transactionType = jsonObject.getString("transactionType");
		String transactionId = jsonObject.getString("transactionId");
		boolean isCompelete = jsonObject.getBooleanValue("isCompelete");
		InetSocketAddress insocket =(InetSocketAddress) ctx.channel().remoteAddress();
		if ("regist".equals(command)) {
			Map<String,ChannelHandlerContext> map = groupThreadMap.get(groupId);
			if (map == null) {
				log.info("map初始化");
				map = new HashMap<>();
			}
			log.info("设置Map：" + insocket.getAddress().getHostAddress() + String.valueOf(insocket.getPort()));
			map.put(insocket.getAddress().getHostAddress() + String.valueOf(insocket.getPort()), ctx);
			groupThreadMap.put(groupId, map);
			if (isCompelete) {
				if ("rollback".equals(transactionType)) {
					// call netty client do rollback;
					this.callNettyClient(groupId, "rollback");
				} else {
					// call netty client do commit;
					this.callNettyClient(groupId, "commit");
				}
			} else {
				if ("rollback".equals(transactionType)) {
					// call netty client do rollback;
					this.callNettyClient(groupId, "rollback");
				}
			}
		}
    }

    /**
     * 发生异常触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    
    private void callNettyClient(String groupId,String command) {
    	Map<String,ChannelHandlerContext> map = groupThreadMap.get(groupId);
    	map.forEach((ip,ctx) -> {
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("groupId", groupId);
    		jsonObject.put("transactionType", command);
    		log.info("ip:"+ ip + " 回调：" + jsonObject.toJSONString());
    		ctx.writeAndFlush(jsonObject.toJSONString());
    	});
    }
}
