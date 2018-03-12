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
package com.farsunset.lvxin.cim.push;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.repository.GroupMemberRepository;
import com.farsunset.lvxin.util.MessageUtil;
import com.farsunset.lvxin.util.UUIDTools;

/**
 * 推送群消息
 */
@Component
public class GroupMessagePusher extends CIMMessagePusher {

	@Autowired
	private GroupMemberRepository groupMemberRepository;

	/**
	 * 推送聊天消息
	 */
	@Override
	@Async
	public void push(final Message msg) {

		String sender = msg.getSender();
		String groupId = msg.getReceiver();
		List<Message> messageList = new ArrayList<Message>();
		messageList.add(msg);

		List<String> memberList = groupMemberRepository.getAccountList(Long.valueOf(groupId));
		memberList.remove(sender);
		for (String account : memberList) {
			Message message = MessageUtil.clone(msg);
			message.setSender(groupId);
			message.setMid(UUIDTools.getUUID());
			message.setTitle(sender);
			message.setAction(Message.ACTION_3);
			message.setReceiver(account);
			messageList.add(message);

			super.push(message);
		}

		messageService.saveAll(messageList);

	}

	/**
	 * 推送事件类广播消息给所有群成员
	 */
	@Async
	public void pushEvent(final Message msg) {

		List<String> memberList = groupMemberRepository.getAccountList(Long.valueOf(msg.getSender()));
		List<Message> messageList = new ArrayList<Message>();

		for (String account : memberList) {
			Message message = MessageUtil.clone(msg);
			message.setMid(UUIDTools.getUUID());
			message.setReceiver(account);
			messageList.add(message);
			super.push(message);
		}

		messageService.saveAll(messageList);

	}

	/**
	 * 推送事件类广播消息给指定的群成员
	 */
	@Async
	public void pushEvent(final Message msg, final List<GroupMember> memberList) {

		List<Message> messageList = new ArrayList<Message>();

		for (GroupMember member : memberList) {
			Message message = MessageUtil.clone(msg);
			message.setMid(UUIDTools.getUUID());
			message.setReceiver(member.getAccount());
			messageList.add(message);
			GroupMessagePusher.super.push(message);
		}

		messageService.saveAll(messageList);
	}

}
