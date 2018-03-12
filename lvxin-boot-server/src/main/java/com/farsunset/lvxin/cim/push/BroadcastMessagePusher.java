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

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.util.MessageUtil;
import com.farsunset.lvxin.util.UUIDTools;

/**
 * 推送批量用户消息
 */
@Component
public class BroadcastMessagePusher extends CIMMessagePusher {

	/**
	 * 推送全部用户
	 */
	@Override
	@Async
	public void push(Message msg) {

		List<String> accounts = userService.getAccounts();
		List<Message> messageList = new ArrayList<Message>();
		for (String account : accounts) {
			Message message = MessageUtil.clone(msg);
			message.setMid(UUIDTools.getUUID());
			message.setReceiver(account);
			
			messageList.add(message);
			
			super.push(message);
		}
		messageService.saveAll(messageList);
	}

	/**
	 * 推送指定的一些用户
	 */
	@Async
	public void push(final List<String> accountList, final Message msg) {

		List<Message> messageList = new ArrayList<Message>();
		for (String account : accountList) {
			Message message = MessageUtil.clone(msg);
			message.setMid(UUIDTools.getUUID());
			message.setReceiver(account);
			
			messageList.add(message);
			
			super.push(message);
		}
		messageService.saveAll(messageList);
	}

	/**
	 * 推送全部在线的用户
	 */
	@Async
	public void pushOnline(final Message msg) {

		List<CIMSession> sessionList = cimSessionService.queryOnlineList();
		List<Message> messageList = new ArrayList<Message>();
		for (CIMSession session : sessionList) {
			Message message = MessageUtil.clone(msg);
			message.setMid(UUIDTools.getUUID());
			message.setReceiver(session.getAccount());
			
			messageList.add(message);

			super.push(MessageUtil.transform(message), session);
		}
		messageService.saveAll(messageList);
	}
}
