package com.accenture.client.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.client.dao.IOrderDao;
import com.accenture.client.dto.OrderDto;
import com.accenture.common.auth.Authz;
import com.accenture.common.util.JSONUtils;
import com.accenture.common.util.result.CommonResult;

@RestController
@RequestMapping("/api/order")
public class OrderController implements ApplicationContextAware {
	
	@Autowired
	private IOrderDao iOrderDao;
	
	private ApplicationContext applicationContext;
	
	@Authz(value= {"aService","bService"})
	@RequestMapping(value ="/orderInfo",method = RequestMethod.GET)
	public CommonResult<List<Map<String,Object>>> getOrderInfo(@RequestParam("userId") Integer userId) {
		List<Map<String,Object>> orderInfos = this.iOrderDao.getOrderInfoByUserId(userId);
		return CommonResult.success(orderInfos);
	}
	
	@Authz(value= {"bService"})
	@RequestMapping(value ="/orderInfo",method = RequestMethod.POST)
	public CommonResult<List<Map<String,Object>>> saveOrderInfo(@RequestBody OrderDto orderDto) throws Exception {
		this.iOrderDao.saveOrderInfo(orderDto);
		this.iOrderDao.saveOutboxInfo("1", JSONUtils.obj2json(orderDto));
		List<Map<String,Object>> orderInfos = this.iOrderDao.getOrderInfoByUserId(orderDto.getUserId());
		return CommonResult.success(orderInfos);
	}
	
	@Authz(value= {"bService"})
	@RequestMapping(value ="/orderInfo",method = RequestMethod.DELETE)
	public CommonResult<Map<String,Object>> saveOrderInfo(@RequestParam("orderId") Integer orderId) throws Exception {
		Map<String,Object> orderInfo = this.iOrderDao.getOrderInfoByOrderId(orderId);
		this.iOrderDao.deleteOrderInfo(orderId);
		this.iOrderDao.saveOutboxInfo("2", JSONUtils.obj2json(orderInfo));
		return CommonResult.success(null);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
