package com.accenture.client.dao;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface IUserADao {

	public Map<String, Object> getUser(@Param("name") String name);

	public Map<String, Object> getAggregated(@Param("userId") Integer userId);

	public void updateUser(@Param("userId") Integer userId, @Param("money") Integer money);

	public void savePoint(@Param("userId") Integer userId, @Param("money") Integer money,
			@Param("orderTime") Timestamp orderTime);
}
