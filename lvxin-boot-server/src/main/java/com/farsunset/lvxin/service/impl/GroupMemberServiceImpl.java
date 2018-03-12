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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.cim.push.GroupMessagePusher;
import com.farsunset.lvxin.message.builder.Action107Builder;
import com.farsunset.lvxin.message.builder.Action112Builder;
import com.farsunset.lvxin.message.builder.Action113Builder;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.repository.GroupMemberRepository;
import com.farsunset.lvxin.repository.GroupRepository;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.repository.UserRepository;
import com.farsunset.lvxin.service.GroupMemberService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.UUIDTools;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {

	@Autowired
	private GroupMemberRepository groupMemberRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private GroupMessagePusher groupMessagePusher;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<GroupMember> getMemberList(Long groupId) {
		return groupMemberRepository.queryMemberList(groupId);
	}

	@Override
	public List<User> getUserList(Long groupId) {

		return groupMemberRepository.queryMemberUserList(groupId);
	}

	@Override
	public void add(GroupMember groupMember) {
		groupMember.setGid(UUIDTools.getUUID());

		if (groupMemberRepository.exists(Example.of(groupMember))) {
			return;
		}

		User user = userRepository.findOne(groupMember.getAccount());
		Group group = groupRepository.findOne(groupMember.getGroupId());
		Message message = new Message();
		message.setSender(groupMember.getGroupId().toString());
		message.setAction(Constants.MessageAction.ACTION_113);
		message.setContent(new Action113Builder().buildJsonString(user, group));

		groupMessagePusher.pushEvent(message);

		groupMemberRepository.save(groupMember);

	}

	@Override
	public int getMemberCount(Long groupId) {
		return groupMemberRepository.countByGroupId(groupId);
	}

	@Override
	public void remove(GroupMember groupMember) {
		String account = groupMember.getAccount();
		Long groupId = groupMember.getGroupId();
		
		groupMemberRepository.remove(account, groupId);
		messageRepository.deleteGroupMessage(account, groupId.toString());

		User user = userRepository.findOne(account);
		Group group = groupRepository.findOne(groupId);
		Message message = new Message();
		message.setSender(groupId.toString());
		message.setAction(Constants.MessageAction.ACTION_112);
		message.setContent(new Action112Builder().buildJsonString(user, group));

		groupMessagePusher.pushEvent(message);

	}
 
	@Override
	public List<String> getMemberAccountList(Long groupId) {
		// TODO Auto-generated method stub
		return groupMemberRepository.getAccountList(groupId);
	}

	@Override
	public void remove(List<String> list, Long groupId) {

		// 删除和他们相关的消息
		messageRepository.deleteGroupMessage(list, String.valueOf(groupId));

		Group group = groupRepository.findOne(groupId);
		Message message = new Message();
		message.setAction(Constants.MessageAction.ACTION_107);
		message.setSender(groupId.toString());
		message.setContent(new Action107Builder().buildJsonString(group, list));

		List<GroupMember> memberList = getMemberList(groupId);

		groupMessagePusher.pushEvent(message, memberList);

		groupMemberRepository.remove(groupId, list);
	}

}
