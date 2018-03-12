package com.farsunset.lvxin.redis.template;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.model.proto.PubAccountProto;

public class PubAccountRedisTemplate extends RedisTemplate<String, PublicAccount>
		implements RedisSerializer<PublicAccount> {

	private final static String CACHE_PREFIX = "pub_";

	private PubAccountRedisTemplate() {
		StringRedisSerializer stringSerializer = new StringRedisSerializer();

		setKeySerializer(stringSerializer);
		setHashKeySerializer(stringSerializer);

		setValueSerializer(this);
		setHashValueSerializer(this);
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

	public void saveOrRemove(String account, PublicAccount model) {
		if (model == null) {
			remove(account);
		} else {
			save(model);
		}
	}

	@Override
	public byte[] serialize(PublicAccount t) throws SerializationException {
		return t.getProtobufBody();
	}

	@Override
	public PublicAccount deserialize(byte[] bytes) throws SerializationException {

		try {
			PubAccountProto.Model proto = PubAccountProto.Model.parseFrom(bytes);
			PublicAccount account = new PublicAccount();
			account.setAccount(StringUtils.trimToNull(proto.getAccount()));
			account.setName(StringUtils.trimToNull(proto.getName()));
			account.setApiUrl(StringUtils.trimToNull(proto.getApiUrl()));
			account.setDescription(StringUtils.trimToNull(proto.getDescription()));
			account.setGreet(StringUtils.trimToNull(proto.getGreet()));
			account.setLink(StringUtils.trimToNull(proto.getLink()));
			account.setStatus(StringUtils.trimToNull(proto.getStatus()));
			return account;

		} catch (Exception error) {
			return null;
		}
	}

}
