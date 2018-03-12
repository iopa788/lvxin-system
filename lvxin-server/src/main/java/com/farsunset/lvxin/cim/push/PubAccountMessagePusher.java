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

import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.repository.SubscriberRepository;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.MessageUtil;
import com.farsunset.lvxin.util.UUIDTools;

/**
 * 公众号向所有订阅用户发消息
 */
@Component
public class PubAccountMessagePusher extends CIMMessagePusher {

	@Autowired
	private SubscriberRepository subscriberRepository;

	/**
	 * 公众号向所有订阅者推送消息
	 */
	@Override
	@Async
	public void push(final Message msg) {

		List<String> accountList = subscriberRepository.getSubscriberAccounts(msg.getReceiver());

		List<Message> messageList = new ArrayList<Message>();

		for (String account : accountList) {
			Message message = MessageUtil.clone(msg);
			message.setMid(UUIDTools.getUUID());
			message.setReceiver(account);
			message.setSender(msg.getReceiver());
			message.setAction(Constants.MessageAction.ACTION_201);
			messageList.add(message);
			PubAccountMessagePusher.super.push(message);
		}

		messageService.save(messageList);
	}

	/**
	 * 公众号向所有订阅者推送菜单更新事件消息
	 */
	@Async
	public void push(final Message msg, final String pubAccount) {

		List<String> accountList = subscriberRepository.getSubscriberAccounts(pubAccount);

		List<Message> messageList = new ArrayList<Message>();

		for (String account : accountList) {
			Message message = MessageUtil.clone(msg);
			message.setMid(UUIDTools.getUUID());
			message.setReceiver(account);
			message.setSender(pubAccount);
			messageList.add(message);
			PubAccountMessagePusher.super.push(message);
		}

		messageService.save(messageList);
	}
}
