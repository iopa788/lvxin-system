package com.farsunset.lvxin.redis.template;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.cim.sdk.server.model.proto.SessionProto;
import com.google.protobuf.InvalidProtocolBufferException;

public class SessionRedisTemplate extends RedisTemplate<String, CIMSession> {
	final static String CACHE_PREFIX = "cim_";
	public SessionRedisTemplate() {
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<CIMSession> sessionSerializer = new SessionRedisSerializer();

		setKeySerializer(stringSerializer);
		setValueSerializer(sessionSerializer);
		setHashKeySerializer(stringSerializer);
		setHashValueSerializer(sessionSerializer);
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
	public CIMSession get(String uid) {
		String key = CACHE_PREFIX + uid;
		return super.boundValueOps(key).get();
	}
	
	public void remove(String uid) {
		String key = CACHE_PREFIX + uid;
		super.delete(key);
	}
	
	public void saveOrRemove(String account ,CIMSession session) {
		if(session==null) {
			remove(account);
		}else{
			save(session);
		}
	}
	
	class SessionRedisSerializer implements RedisSerializer <CIMSession>
	{

		@Override
		public byte[] serialize(CIMSession t) throws SerializationException {
			return t.getProtobufBody();
		}

		private String getRealValue(String v) {
			return v!=null &&v.isEmpty() ? null : v;
		}
		
		@Override
		public CIMSession deserialize(byte[] bytes) throws SerializationException {
			if(bytes == null) {
				return null;
			}
			try {
				SessionProto.Model proto = SessionProto.Model.parseFrom(bytes);
				CIMSession session = new CIMSession();
				session.setApnsAble(proto.getApnsAble());
				session.setBindTime(proto.getBindTime());
				session.setChannel(getRealValue(proto.getChannel()));
				session.setClientVersion(getRealValue(proto.getClientVersion()));
				session.setDeviceId(getRealValue(proto.getDeviceId()));
				session.setDeviceModel(getRealValue(proto.getDeviceModel()));
				session.setHost(getRealValue(proto.getHost()));
				session.setLatitude(proto.getLatitude());
				session.setLongitude(proto.getLongitude());
				session.setLocation(getRealValue(proto.getLocation()));
				session.setNid(proto.getNid());
				session.setSystemVersion(getRealValue(proto.getSystemVersion()));
				session.setStatus(proto.getStatus());
				session.setAccount(getRealValue(proto.getAccount()));

				return session;
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
