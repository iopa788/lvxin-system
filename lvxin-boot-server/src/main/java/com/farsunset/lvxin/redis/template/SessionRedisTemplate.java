package com.farsunset.lvxin.redis.template;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.cim.sdk.server.model.proto.SessionProto;

public class SessionRedisTemplate extends RedisTemplate<String, CIMSession> implements RedisSerializer<CIMSession> {

	private final static String CACHE_PREFIX = "cim_";

	private SessionRedisTemplate() {
		StringRedisSerializer stringSerializer = new StringRedisSerializer();

		setKeySerializer(stringSerializer);
		setHashKeySerializer(stringSerializer);

		setValueSerializer(this);
		setHashValueSerializer(this);
	}

	public SessionRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	@Override
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}

	public void save(CIMSession session) {
		String key = CACHE_PREFIX + session.getAccount();
		super.boundValueOps(key).set(session);
	}

	public CIMSession get(String account) {
		String key = CACHE_PREFIX + account;
		return super.boundValueOps(key).get();
	}

	private void remove(String account) {
		String key = CACHE_PREFIX + account;
		super.delete(key);
	}

	public void saveOrRemove(String account, CIMSession session) {
		if (session == null) {
			remove(account);
		} else {
			save(session);
		}
	}

	@Override
	public byte[] serialize(CIMSession t) throws SerializationException {
		return t.getProtobufBody();
	}

	@Override
	public CIMSession deserialize(byte[] bytes) throws SerializationException {

		try {
			SessionProto.Model proto = SessionProto.Model.parseFrom(bytes);
			CIMSession session = new CIMSession();
			session.setApnsAble(proto.getApnsAble());
			session.setBindTime(proto.getBindTime());
			session.setChannel(StringUtils.trimToNull(proto.getChannel()));
			session.setClientVersion(StringUtils.trimToNull(proto.getClientVersion()));
			session.setDeviceId(StringUtils.trimToNull(proto.getDeviceId()));
			session.setDeviceModel(StringUtils.trimToNull(proto.getDeviceModel()));
			session.setHost(StringUtils.trimToNull(proto.getHost()));
			session.setLatitude(proto.getLatitude());
			session.setLongitude(proto.getLongitude());
			session.setLocation(StringUtils.trimToNull(proto.getLocation()));
			session.setNid(proto.getNid());
			session.setSystemVersion(StringUtils.trimToNull(proto.getSystemVersion()));
			session.setStatus(proto.getStatus());
			session.setAccount(StringUtils.trimToNull(proto.getAccount()));

			return session;

		} catch (Exception error) {
			return null;
		}

	}

}
