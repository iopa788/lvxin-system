package com.farsunset.lvxin.redis.template;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.model.proto.PubAccountProto;
public class PubAccountRedisTemplate extends RedisTemplate<String, PublicAccount> {
	final static String CACHE_PREFIX = "pub_";
	public PubAccountRedisTemplate() {
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<PublicAccount> accountSerializer = new PubAccountRedisSerializer();
		setKeySerializer(stringSerializer);
		setValueSerializer(accountSerializer);
		setHashKeySerializer(stringSerializer);
		setHashValueSerializer(accountSerializer);
	}
	 
	public PubAccountRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	@Override
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}
	
	public void save(PublicAccount account) {
		String key = CACHE_PREFIX + account.getAccount();
		super.boundValueOps(key).set(account);
	}
	public PublicAccount get(String account) {
		String key = CACHE_PREFIX + account;
		return super.boundValueOps(key).get();
	}
	
	public void remove(String account) {
		String key = CACHE_PREFIX + account;
		super.delete(key);
	}
	
	public void saveOrRemove(String account ,PublicAccount model) {
		if(model==null) {
			remove(account);
		}else{
			save(model);
		}
	}
	
	class PubAccountRedisSerializer implements RedisSerializer <PublicAccount>
	{
		@Override
		public byte[] serialize(PublicAccount t) throws SerializationException {
			return t.getProtobufBody();
		}

		private String getRealValue(String v) {
			return v!=null &&v.isEmpty() ? null : v;
		}
		
		@Override
		public PublicAccount deserialize(byte[] bytes) throws SerializationException {
			 
			try {
				PubAccountProto.Model proto = PubAccountProto.Model.parseFrom(bytes);
				PublicAccount account = new PublicAccount();
				account.setAccount(getRealValue(proto.getAccount()));
				account.setName(getRealValue(proto.getName()));
				account.setApiUrl(getRealValue(proto.getApiUrl()));
				account.setDescription(getRealValue(proto.getDescription()));
				account.setGreet(getRealValue(proto.getGreet()));
				account.setLink(getRealValue(proto.getLink()));
				account.setStatus(getRealValue(proto.getStatus()));
				return account;
				
			} catch (Exception error) {}
			
			return null;
		}
		
	}
}
