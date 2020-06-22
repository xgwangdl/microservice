package com.accenture.client.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface IOrderDao {
	public List<Map<String,Object>> getOrderInfoByUserId(@Param("userId") Integer userId);
}
