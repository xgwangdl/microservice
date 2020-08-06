package com.accenture.client.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.accenture.client.dto.OrderDto;

public interface IOrderDao {
	public List<Map<String, Object>> getOrderInfoByUserId(@Param("userId") Integer userId);

	public Map<String, Object> getOrderInfoByOrderId(@Param("orderId") Integer orderId);
	
	public void saveOrderInfo(@Param("order") OrderDto orderDto);

	public void saveOutboxInfo(@Param("type") String type, @Param("content") String content);

	public void deleteOrderInfo(@Param("orderId") Integer orderId);
	
	public List<Map<String, Object>> getOutboxInfoByNotProcess();
	
	public void updateOutboxInfo(@Param("id") Integer id,@Param("status") String status);
}
