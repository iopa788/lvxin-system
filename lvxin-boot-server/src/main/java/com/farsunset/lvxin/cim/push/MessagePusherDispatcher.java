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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.util.Constants;

@Component
public class MessagePusherDispatcher {
	@Autowired
	DefaultMessagePusher defaultMessagePusher;

	@Autowired
	GroupMessagePusher groupMessagePusher;

	@Autowired
	PubAccountMessagePusher pubAccountMessagePusher;

	public void push(Message message) {
		getMessagePusher(message.getAction()).push(message);

	}

	private CIMMessagePusher getMessagePusher(String action) {
		if (Constants.MessageAction.ACTION_0.equals(action)) {
			return defaultMessagePusher;
		} else if (Constants.MessageAction.ACTION_1.equals(action)) {
			return groupMessagePusher;
		} else if (Constants.MessageAction.ACTION_200.equals(action)) {
			return pubAccountMessagePusher;
		}
		return defaultMessagePusher;
	}

}
