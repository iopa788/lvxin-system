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
import com.farsunset.lvxin.service.MomentRuleService;
import com.farsunset.lvxin.util.MessageUtil;
import com.farsunset.lvxin.util.UUIDTools;

/**
 * 推送圈子消息 过滤屏蔽我的人以及被我屏蔽的人
 */
@Component
public class MomentMessagePusher extends CIMMessagePusher {

	@Autowired
	MomentRuleService momentRuleService;

	@Override
	@Async
	public void push(Message msg) {

		List<String> accountList = momentRuleService.queryFilteredList(msg.getSender());

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

}
