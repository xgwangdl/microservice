package com.accenture.backendservice.dao;

import org.apache.ibatis.annotations.Param;

import com.accenture.backendservice.dto.AggregatedDto;

public interface IAggregatedDao {
	public void mergeAggregated(@Param("aggregatedDto")AggregatedDto aggregatedDto);
	
	public void insertAggregatedDtl(@Param("aggregatedDto")AggregatedDto aggregatedDto);
}
