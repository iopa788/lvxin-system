package com.farsunset.lvxin.redis.template;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.proto.GroupProto;

public class GroupRedisTemplate extends RedisTemplate<String, Group> implements RedisSerializer<Group> {

	private final static String CACHE_PREFIX = "group_";

	private GroupRedisTemplate() {
		StringRedisSerializer stringSerializer = new StringRedisSerializer();

		setKeySerializer(stringSerializer);
		setHashKeySerializer(stringSerializer);

		setValueSerializer(this);
		setHashValueSerializer(this);
	}

	public GroupRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	@Override
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}

	public void save(Group user) {
		String key = CACHE_PREFIX + user.getGroupId();
		super.boundValueOps(key).set(user);
	}

	public Group get(long groupId) {
		String key = CACHE_PREFIX + groupId;
		return super.boundValueOps(key).get();
	}

	public void remove(long groupId) {
		String key = CACHE_PREFIX + groupId;
		super.delete(key);
	}

	public void saveOrRemove(long groupId, Group group) {
		if (group == null) {
			remove(groupId);
		} else {
			save(group);
		}
	}

	@Override
	public byte[] serialize(Group t) throws SerializationException {
		return t.getProtobufBody();
	}

	@Override
	public Group deserialize(byte[] bytes) throws SerializationException {

		try {
			GroupProto.Model proto = GroupProto.Model.parseFrom(bytes);
			Group group = new Group();
			group.setGroupId(proto.getGroupId());
			group.setName(StringUtils.trimToNull(proto.getName()));
			group.setFounder(StringUtils.trimToNull(proto.getFounder()));
			group.setSummary(StringUtils.trimToNull(proto.getSummary()));
			group.setCategory(StringUtils.trimToNull(proto.getCategory()));

			return group;

		} catch (Exception error) {
			return  null;
		}
	}
}
