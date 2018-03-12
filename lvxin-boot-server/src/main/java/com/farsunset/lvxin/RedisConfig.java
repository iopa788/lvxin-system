package com.farsunset.lvxin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.farsunset.lvxin.redis.template.GroupRedisTemplate;
import com.farsunset.lvxin.redis.template.PubAccountRedisTemplate;
import com.farsunset.lvxin.redis.template.SessionRedisTemplate;
import com.farsunset.lvxin.redis.template.TokenRedisTemplate;
import com.farsunset.lvxin.redis.template.UserRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableAutoConfiguration
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.pool.max-active}")
	private int maxActive;

	@Value("${spring.redis.pool.min-idle}")
	private int minIdle;

	@Value("${spring.redis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.pool.max-wait}")
	private int maxWait;

	@Bean
	public JedisPoolConfig getRedisConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(maxIdle);
		config.setMinIdle(minIdle);
		config.setMaxTotal(maxActive);
		config.setMaxWaitMillis(maxWait);
		return config;
	}

	@Bean
	public JedisConnectionFactory getConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(host);
		factory.setPassword(password);
		factory.setPort(port);
		factory.setTimeout(timeout);
		factory.setPoolConfig(getRedisConfig());
		return factory;
	}

	@Bean
	public SessionRedisTemplate getSessionRedisTemplate() {
		return new SessionRedisTemplate(getConnectionFactory());
	}

	@Bean
	public UserRedisTemplate getUserRedisTemplate() {
		return new UserRedisTemplate(getConnectionFactory());
	}

	@Bean
	public TokenRedisTemplate getTokenRedisTemplate() {
		return new TokenRedisTemplate(getConnectionFactory());
	}

	@Bean
	public PubAccountRedisTemplate getPubAccountRedisTemplate() {
		return new PubAccountRedisTemplate(getConnectionFactory());
	}

	@Bean
	public GroupRedisTemplate getGroupRedisTemplate() {
		return new GroupRedisTemplate(getConnectionFactory());
	}
}