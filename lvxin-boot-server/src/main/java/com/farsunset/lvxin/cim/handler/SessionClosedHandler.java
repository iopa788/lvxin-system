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
package com.farsunset.lvxin.cim.handler;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.farsunset.cim.sdk.server.constant.CIMConstant;
import com.farsunset.cim.sdk.server.handler.CIMRequestHandler;
import com.farsunset.cim.sdk.server.model.ReplyBody;
import com.farsunset.cim.sdk.server.model.SentBody;
import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.cim.push.BroadcastMessagePusher;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.service.impl.CIMSessionServiceImpl;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.UUIDTools;

/**
 * 
 * 连接断开时，更新用户相关状态
 *
 */
@Component
public class SessionClosedHandler implements CIMRequestHandler {

	private final Logger logger = Logger.getLogger(SessionClosedHandler.class.getName());
	@Autowired
	private CIMSessionServiceImpl cimSessionService;

	@Autowired
	private UserService userServiceImpl;

	@Value("${sys.online.borcast}")
	private String borcastSwitch;

	@Autowired
	BroadcastMessagePusher broadcastMessagePusher;

	@Override
	public ReplyBody process(CIMSession ios, SentBody message) {

		Object account = ios.getAttribute(CIMConstant.SESSION_KEY);
		if (account == null) {
			return null;
		}

		logger.info(
				"sessionClosed()... nid:" + ios.getNid() + " account:" + account + " isConnected:" + ios.isConnected());

		updateCIMSessionStatus(account.toString());
		updateUserOnlineStatus(account.toString());
		broadcastOfflineMessage(account.toString());
		return null;
	}

	private void updateCIMSessionStatus(String account) {

		CIMSession oldSession = cimSessionService.get(account);
		if (oldSession != null) {
			oldSession.setStatus(CIMSession.STATUS_DISABLED);
			oldSession.setNid(null);
			cimSessionService.save(oldSession);
		}

	}

	private void updateUserOnlineStatus(String account) {
		User target = userServiceImpl.get(account);
		target.setOnline(User.OFF_LINE);
		userServiceImpl.updateQuietly(target);
	}

	private void broadcastOfflineMessage(String account) {
		if ("1".equals(borcastSwitch)) {
			return;
		}

		com.farsunset.lvxin.model.Message offlineMessage = new com.farsunset.lvxin.model.Message();
		offlineMessage.setAction(Constants.MessageAction.ACTION_900);
		offlineMessage.setContent(account);
		offlineMessage.setSender(Constants.SYSTEM);
		offlineMessage.setMid(UUIDTools.getUUID());
		broadcastMessagePusher.pushOnline(offlineMessage);
	}

}
