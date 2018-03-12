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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.farsunset.cim.sdk.server.model.Message;
import com.farsunset.lvxin.service.ApnsService;
import com.farsunset.lvxin.util.Constants;

import javapns.Push;
import javapns.notification.PushNotificationPayload;

@Service
public class JavaApnsServiceImpl implements ApnsService {
	protected final Logger logger = Logger.getLogger(JavaApnsServiceImpl.class);
	@Value("${apple.apns.p12.password}")
	private String password;
	@Value("${apple.apns.p12.file}")
	private String keystore;
	@Value("${apple.apns.debug}")
	private boolean isDebug;
	@Override
	public void push(int badge, Message message, String deviceToken) {

		// 只提示聊天相关消息
		if (!message.getAction().equals(Constants.MessageAction.ACTION_0)
				&& message.getAction().equals(Constants.MessageAction.ACTION_2)
				&& message.getAction().equals(Constants.MessageAction.ACTION_3)
				&& message.getAction().equals(Constants.MessageAction.ACTION_201)
				&& message.getAction().equals(Constants.MessageAction.ACTION_202)
				&& message.getAction().equals(Constants.MessageAction.ACTION_700)) {

			return;
		}


		PushNotificationPayload payload = PushNotificationPayload.complex();
		try {
			payload.addAlert(message.getContent());
			payload.addBadge(1);
			payload.addSound("default");
			Push.payload(payload, keystore, password, isDebug, deviceToken);

		} catch (Exception e) {
			logger.error("apns推送失败", e);
			e.printStackTrace();
		}
	}
}
