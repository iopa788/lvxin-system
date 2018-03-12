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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.farsunset.lvxin.cim.push.GroupMessagePusher;
import com.farsunset.lvxin.message.builder.Action104Builder;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.repository.GroupMemberRepository;
import com.farsunset.lvxin.repository.GroupRepository;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.service.GroupService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.UUIDTools;
import com.farsunset.lvxin.web.jstl.Page;

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

	@Override
	public void add(Group group) {

		groupRepository.save(group);
		GroupMember member = new GroupMember();
		member.setAccount(group.getFounder());
		member.setGroupId(group.getGroupId());
		member.setHost(GroupMember.RULE_FOUNDER);
		member.setGid(UUIDTools.getUUID());
		groupMemberRepository.save(member);

	}

	@Override
	public List<Group> getRelevantList(String account) {

		List<Group> list = groupRepository.getRelevantList(account);
		for (Group g : list) {
			Set<GroupMember> memberList = new HashSet<GroupMember>();
			memberList.addAll(groupMemberRepository.getMemberList(g.getGroupId()));
			g.setMemberList(memberList);
		}
		return list;
	}

	@Override
	public Group queryById(long groupId) {
		return groupRepository.get(groupId);
	}

	@Override
	public void update(Group group) {
		groupRepository.update(group);

		messageRepository.deleteBySenderAndAction(group.getGroupId().toString(), Constants.MessageAction.ACTION_114);

		Message message = new Message();
		message.setSender(group.getGroupId().toString());
		message.setAction(Constants.MessageAction.ACTION_114);
		message.setContent(JSON.toJSONString(group));
		message.setReceiver(group.getGroupId().toString());

		groupMessagePusher.pushEvent(message);
	}

	@Override
	public void delete(String account, long groupId) {
		Group group = groupRepository.get(groupId);
		groupRepository.delete(group);

		messageRepository.deleteGroupMessage(String.valueOf(groupId));

		List<GroupMember> list = groupMemberRepository.getMemberList(groupId, account);
		Message message = new Message();
		message.setSender(Constants.SYSTEM);
		message.setAction(Constants.MessageAction.ACTION_104);
		message.setContent(new Action104Builder().buildJsonString(groupId, group.getName()));

		groupMessagePusher.pushEvent(message, list);

		groupMemberRepository.clean(groupId);
	}

	@Override
	public void queryPage(Group group, Page page) {
		int count = this.groupRepository.queryAmount(group);
		page.setCount(Integer.valueOf(count));
		if (page.getCount() == 0) {
			return;
		}
		this.groupRepository.queryPage(group, page);
	}

	@Override
	public List<Group> queryAll() {
		return groupRepository.getAll();
	}

	@Override
	public User queryFounder(long groupId) {
		// TODO Auto-generated method stub
		return groupRepository.queryFounder(groupId);
	}
}
