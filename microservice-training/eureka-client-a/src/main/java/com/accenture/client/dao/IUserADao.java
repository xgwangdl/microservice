package com.accenture.client.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface IUserADao {

	public Map<String,Object> getUser(@Param("name") String name);
}
