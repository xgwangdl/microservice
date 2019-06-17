package com.accenture.client.dao;

import org.apache.ibatis.annotations.Param;

public interface IClassBDao {
	public String getClassName(@Param("id") String id);
}
