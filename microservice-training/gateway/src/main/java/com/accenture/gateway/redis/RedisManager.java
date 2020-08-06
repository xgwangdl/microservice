package com.accenture.gateway.redis;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisManager {

	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	
	public Integer increValue(String key) {
		ValueOperations<String, Serializable> valueOperations = redisTemplate.opsForValue();
		valueOperations.increment(key);
		return (Integer)valueOperations.get(key);
	}
	
	public void putValueExpireTimes(String key,Integer value,Long expireTimes) {
		ValueOperations<String, Serializable> valueOperations = this.redisTemplate.opsForValue();
		valueOperations.set(key, value);
		this.redisTemplate.expire(key, expireTimes, TimeUnit.SECONDS);
	}
	
	public void deleteValue(String key) {
		this.redisTemplate.delete(key);
	}
	
	public Integer getValue(String key) {
		ValueOperations<String, Serializable> valueOperations = redisTemplate.opsForValue();
		return (Integer)valueOperations.get(key);
	}
	
	public Long getExpire(String key) {
		return this.redisTemplate.getExpire(key);
	}
}
