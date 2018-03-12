/**
 * Copyright 2013-2033 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.lvxin.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.cim.push.GroupMessagePusher;
import com.farsunset.lvxin.message.builder.Action104Builder;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.redis.template.GroupRedisTemplate;
import com.farsunset.lvxin.repository.GroupMemberRepository;
import com.farsunset.lvxin.repository.GroupRepository;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.service.GroupService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.UUIDTools;
import com.google.gson.Gson;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private GroupMemberRepository groupMemberRepository;

	@Autowired
	private GroupMessagePusher groupMessagePusher;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private GroupRedisTemplate groupRedisTemplate;

	@Override
	public void add(Group group) {

		groupRepository.save(group);
		GroupMember member = new GroupMember();
		member.setAccount(group.getFounder());
		member.setGroupId(group.getGroupId());
		member.setHost(GroupMember.RULE_FOUNDER);
		member.setGid(UUIDTools.getUUID());
		groupMemberRepository.save(member);
		groupRedisTemplate.save(group);
	}

	@Override
	public List<Group> getRelevantList(String account) {

		List<Group> list = groupRepository.getRelevantList(account);
		for (Group g : list) {
			Set<GroupMember> memberList = new HashSet<GroupMember>(groupMemberRepository.queryMemberList(g.getGroupId()));
			g.setMemberList(memberList);
		}
		return list;
	}

	@Override
	public Group queryById(long groupId) {

		Group group = groupRedisTemplate.get(groupId);

		if (group == null) {
			group = groupRepository.findOne(groupId);
			groupRedisTemplate.saveOrRemove(groupId, group);
		}

		return group;
	}

	@Override
	public void update(Group group) {

		groupRepository.saveAndFlush(group);
		groupRedisTemplate.save(group);

		messageRepository.deleteBySenderAndAction(group.getGroupId().toString(), Constants.MessageAction.ACTION_114);

		Message message = new Message();
		message.setSender(group.getGroupId().toString());
		message.setAction(Constants.MessageAction.ACTION_114);
		message.setContent(new Gson().toJson(group));
		message.setReceiver(group.getGroupId().toString());

		groupMessagePusher.pushEvent(message);
	}

	@Override
	public void delete(String account, long groupId) {
		Group group = queryById(groupId);
		groupRepository.delete(group);

		messageRepository.deleteGroupMessage(String.valueOf(groupId));

		List<GroupMember> list = groupMemberRepository.queryMemberList(groupId, account);
		Message message = new Message();
		message.setSender(Constants.SYSTEM);
		message.setAction(Constants.MessageAction.ACTION_104);
		message.setContent(new Action104Builder().buildJsonString(groupId, group.getName()));

		groupMessagePusher.pushEvent(message, list);

		groupRedisTemplate.remove(groupId);
		groupMemberRepository.clean(groupId);
	}

	@Override
	public Page<Group> queryPage(Group group, Pageable pageable) {
		Specification<Group> specification = new Specification<Group>() {

			@Override
			public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				if (!StringUtils.isEmpty(group.getName())) {
					predicatesList.add(builder.equal(root.get("name").as(String.class), group.getName()));
				}
				if (null != (group.getGroupId())) {
					predicatesList.add(builder.equal(root.get("groupId").as(Long.class), group.getGroupId()));
				}
				if (StringUtils.isNotEmpty(group.getFounder())) {
					predicatesList.add(builder.equal(root.get("founder").as(String.class), group.getFounder()));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				query.orderBy(builder.desc(root.get("groupId").as(Long.class)));
				return query.getRestriction();
			}
		};
		return groupRepository.findAll(specification, pageable);
	}

	@Override
	public List<Group> queryAll() {
		return groupRepository.findAll();
	}

	@Override
	public User queryFounder(long groupId) {
		return groupRepository.queryFounder(groupId);
	}
}
