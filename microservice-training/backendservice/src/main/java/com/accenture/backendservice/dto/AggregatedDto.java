package com.accenture.backendservice.dto;

import java.sql.Timestamp;
import java.util.List;

public class AggregatedDto {

	/**User ID*/
	private Integer userId;
	
	/**登陆时间*/
	private List<Timestamp> loginTimes;
	
	/**计数*/
	private Integer count;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public List<Timestamp> getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(List<Timestamp> loginTimes) {
		this.loginTimes = loginTimes;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}
