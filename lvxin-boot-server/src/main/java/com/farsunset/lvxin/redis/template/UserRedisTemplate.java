package com.farsunset.lvxin.redis.template;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.model.proto.UserProto;

public class UserRedisTemplate extends RedisTemplate<String, User> implements RedisSerializer<User> {

	private final static String CACHE_PREFIX = "uid_";

	private UserRedisTemplate() {
		StringRedisSerializer stringSerializer = new StringRedisSerializer();

		setKeySerializer(stringSerializer);
		setHashKeySerializer(stringSerializer);

		setValueSerializer(this);
		setHashValueSerializer(this);
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

	public User get(String account) {
		String key = CACHE_PREFIX + account;
		return super.boundValueOps(key).get();
	}

	public void remove(String account) {
		String key = CACHE_PREFIX + account;
		super.delete(key);
	}

	public void saveOrRemove(String account, User user) {
		if (user == null) {
			remove(account);
		} else {
			save(user);
		}
	}

	@Override
	public byte[] serialize(User t) throws SerializationException {
		return t.getProtobufBody();
	}

	@Override
	public User deserialize(byte[] bytes) throws SerializationException {

		try {
			UserProto.Model proto = UserProto.Model.parseFrom(bytes);
			User user = new User();
			user.setAccount(StringUtils.trimToNull(proto.getAccount()));
			user.setName(StringUtils.trimToNull(proto.getName()));
			user.setTelephone(StringUtils.trimToNull(proto.getTelephone()));
			user.setPassword(StringUtils.trimToNull(proto.getPassword()));
			user.setOrgCode(StringUtils.trimToNull(proto.getOrgCode()));
			user.setEmail(StringUtils.trimToNull(proto.getEmail()));
			user.setGender(proto.getGender());
			user.setMotto(StringUtils.trimToNull(proto.getMotto()));
			user.setOnline(StringUtils.trimToNull(proto.getOnline()));
			user.setFeature(StringUtils.trimToNull(proto.getFeature()));
			user.setGrade(proto.getGrade());
			user.setLatitude(proto.getLatitude());
			user.setLongitude(proto.getLongitude());
			user.setLocation(StringUtils.trimToNull(proto.getLocation()));
			user.setState(StringUtils.trimToNull(proto.getState()));

			return user;

		} catch (Exception error) {
			return null;
		}
	}

}
