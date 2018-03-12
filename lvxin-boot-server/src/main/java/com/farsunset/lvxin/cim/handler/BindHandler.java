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

import java.util.Objects;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.farsunset.cim.sdk.server.constant.CIMConstant;
import com.farsunset.cim.sdk.server.handler.CIMRequestHandler;
import com.farsunset.cim.sdk.server.model.Message;
import com.farsunset.cim.sdk.server.model.ReplyBody;
import com.farsunset.cim.sdk.server.model.SentBody;
import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.cim.push.BroadcastMessagePusher;
import com.farsunset.lvxin.cim.push.DefaultMessagePusher;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.service.impl.CIMSessionServiceImpl;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.UUIDTools;

/**
 * 客户长连接 账户绑定实现
 */
@Component
public class BindHandler implements CIMRequestHandler {

	private final Logger logger = Logger.getLogger(BindHandler.class.getName());
	@Value("${sys.online.borcast}")
	private String borcastSwitch;
	@Value("${server.host}")
	private String host;

	@Autowired
	private CIMSessionServiceImpl cimSessionService;

	@Autowired
	private UserService userServiceImpl;

	@Autowired
	DefaultMessagePusher defaultMessagePusher;

	@Autowired
	BroadcastMessagePusher broadcastMessagePusher;

	@Override
	public ReplyBody process(CIMSession newSession, SentBody message) {

		ReplyBody reply = new ReplyBody();
		reply.setCode(CIMConstant.ReturnCode.CODE_200);

		try {

			String account = message.get("account");
			newSession.setAccount(account);
			newSession.setDeviceId(message.get("deviceId"));
			newSession.setHost(host);
			newSession.setChannel(message.get("channel"));
			newSession.setDeviceModel(message.get("device"));
			newSession.setClientVersion(message.get("version"));
			newSession.setSystemVersion(message.get("osVersion"));
			newSession.setBindTime(System.currentTimeMillis());
			/**
			 * 由于客户端断线服务端可能会无法获知的情况，客户端重连时，需要关闭旧的连接
			 */
			CIMSession oldSession = cimSessionService.get(account);

			// 如果是账号已经在另一台终端登录。则让另一个终端下线

			if (oldSession != null && !newSession.equals(oldSession) && oldSession.isConnected()) {
				sendForceOfflineMessage(oldSession, account, newSession.getDeviceModel());
			}

			// 第一次设置心跳时间为登录时间
			newSession.setBindTime(System.currentTimeMillis());

			updateUserOnlineStatus(account);

			cimSessionService.save(newSession);
			logger.info("bind successful account:" + account + " nid:" + newSession.getNid());

			broadcastOnlineMessage(account);

		} catch (Exception e) {
			reply.setCode(CIMConstant.ReturnCode.CODE_500);
			reply.setMessage(e.getMessage());
			logger.warning("bind failed account:" + message.get("account") + " nid:" + newSession.getNid());
		}
		return reply;
	}

	private void updateUserOnlineStatus(String account) {
		// 设置在线状态
		User target = userServiceImpl.get(account);
		target.setAccount(account);
		target.setOnline(User.ON_LINE);
		userServiceImpl.updateQuietly(target);

	}

	private void sendForceOfflineMessage(CIMSession oldSession, String account, String deviceModel) {

		Message msg = new Message();
		msg.setMid(UUIDTools.getUUID());
		msg.setAction(CIMConstant.MessageAction.ACTION_999);// 强行下线消息类型
		msg.setReceiver(account);
		msg.setSender(Constants.SYSTEM);
		msg.setContent(deviceModel);
		defaultMessagePusher.push(msg, oldSession);

		closeQuietly(oldSession);

	}

	// 不同设备同一账号登录时关闭旧的连接
	private void closeQuietly(CIMSession oldSession) {
		if (oldSession.isConnected() && Objects.equals(host, oldSession.getHost())) {
			oldSession.removeAttribute(CIMConstant.SESSION_KEY);
			oldSession.closeOnFlush();
		}
	}

	private void broadcastOnlineMessage(String account) {
		if ("1".equals(borcastSwitch)) {
			return;
		}

		com.farsunset.lvxin.model.Message onlineMessage = new com.farsunset.lvxin.model.Message();
		onlineMessage.setAction(Constants.MessageAction.ACTION_901);
		onlineMessage.setContent(account);
		onlineMessage.setSender(Constants.SYSTEM);
		onlineMessage.setMid(UUIDTools.getUUID());
		broadcastMessagePusher.pushOnline(onlineMessage);
	}

}
