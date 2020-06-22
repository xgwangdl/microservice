package com.accenture.client.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.client.dao.IOrderDao;
import com.accenture.common.auth.Authz;
import com.accenture.common.exception.RestException;
import com.accenture.common.util.ApplicationContext;
import com.accenture.common.util.result.CommonResult;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	@Autowired
	private IOrderDao iOrderDao;
	
	@Authz(value= {"aService","bService"})
	@RequestMapping(value ="/getOrderInfo",method = RequestMethod.GET)
	public CommonResult<List<Map<String,Object>>> getOrderInfo(@RequestParam("userId") Integer userId) {
		if (StringUtils.isEmpty(ApplicationContext.getUserName())) {
			throw new RestException("401","not login user");
		}
		List<Map<String,Object>> orderInfos = this.iOrderDao.getOrderInfoByUserId(userId);
		return CommonResult.success(orderInfos);
	}
}
