package com.farsunset.lvxin.redis.template;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

public class TokenRedisTemplate extends StringRedisTemplate {

	public TokenRedisTemplate(RedisConnectionFactory connectionFactory) {
		super(connectionFactory);
	}

	public void save(String key, String value) {
		super.boundValueOps(key).set(value);
	}

	public String get(String key) {
		return super.boundValueOps(key).get();
	}

	public void remove(String key) {
		super.delete(key);
	}
}
