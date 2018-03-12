package com.farsunset.lvxin.redis.template;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.model.proto.UserProto;
import com.google.protobuf.InvalidProtocolBufferException;
public class UserRedisTemplate extends RedisTemplate<String, User> {
	final static String CACHE_PREFIX = "uid_";
	public UserRedisTemplate() {
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<User> userSerializer = new UserRedisSerializer();
		setKeySerializer(stringSerializer);
		setValueSerializer(userSerializer);
		setHashKeySerializer(stringSerializer);
		setHashValueSerializer(userSerializer);
	}
	 
	public UserRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	@Override
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}
	
	public void save(User user) {
		String key = CACHE_PREFIX + user.getAccount();
		super.boundValueOps(key).set(user);
	}
	public User get(String uid) {
		String key = CACHE_PREFIX + uid;
		return super.boundValueOps(key).get();
	}
	
	public void remove(String uid) {
		String key = CACHE_PREFIX + uid;
		super.delete(key);
	}
	
	public void saveOrRemove(String account, User user) {
		if (user == null) {
			remove(account);
		} else {
			save(user);
		}
	}
	
	class UserRedisSerializer implements RedisSerializer <User>
	{
		@Override
		public byte[] serialize(User t) throws SerializationException {
			return t.getProtobufBody();
		}

		private String getRealValue(String v) {
			return v!=null &&v.isEmpty() ? null : v;
		}
		
		@Override
		public User deserialize(byte[] bytes) throws SerializationException {
			if(bytes == null) {
				return null;
			}
			try {
				UserProto.Model proto = UserProto.Model.parseFrom(bytes);
				User user = new User();
				user.setAccount(getRealValue(proto.getAccount()));
				user.setName(getRealValue(proto.getName()));
				user.setTelephone(getRealValue(proto.getTelephone()));
				user.setPassword(getRealValue(proto.getPassword()));
				user.setOrgCode(getRealValue(proto.getOrgCode()));
				user.setEmail(getRealValue(proto.getEmail()));
				user.setGender(proto.getGender());
				user.setMotto(getRealValue(proto.getMotto()));
				user.setOnline(getRealValue(proto.getOnline()));
				user.setFeature(getRealValue(proto.getFeature()));
				user.setGrade(proto.getGrade());
				user.setLatitude(proto.getLatitude());
				user.setLongitude(proto.getLongitude());
				user.setLocation(getRealValue(proto.getLocation()));
				user.setState(getRealValue(proto.getState()));

				return user;
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
