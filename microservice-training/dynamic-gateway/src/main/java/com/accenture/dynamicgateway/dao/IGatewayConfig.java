package com.accenture.dynamicgateway.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.accenture.dynamicgateway.dto.GetwayConfigDTO;

public interface IGatewayConfig {
	public List<GetwayConfigDTO> findAll();
	public void save(@Param("getwayConfigDTO") GetwayConfigDTO getwayConfigDTO);
	public void deleteById(@Param("id") String id); 
}
