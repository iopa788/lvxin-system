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

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.service.ApnsService;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.service.impl.CIMSessionServiceImpl;
import com.farsunset.lvxin.util.MessageDispatcher;
import com.farsunset.lvxin.util.MessageUtil;

@Component
public class CIMMessagePusher {

	@Value("${cim.server.host}") 
	private String host;
	
	@Value("${sys.message.dispatch.url}") 
	private String dispatchUrl;
	
	@Autowired
	protected CIMSessionServiceImpl cimSessionService;
	
	@Autowired
	private ApnsService apnsService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected MessageService messageService;


	
	
	/**
	 * 向用户发送消息
	 * 
	 * @param msg
	 */
	public void push(com.farsunset.cim.sdk.server.model.Message msg) {
		CIMSession session = cimSessionService.get(msg.getReceiver());
		push(msg, session);
	}

	/**
	 * 向用户发送消息
	 * 
	 * @param msg
	 */
	public void push(com.farsunset.cim.sdk.server.model.Message msg, CIMSession session) {

		if(session == null) {
			return ;
		}
		/*
		 * 服务器集群时，可以在此 判断当前session是否连接于本台服务器，如果是，继续往下走，如果不是，将此消息发往当前session连接的服务器并
		 * return
		 */
		if (session.isConnected() && !Objects.equals(host, session.getHost())) {// 判断当前session是否连接于本台服务器，如不是
			// 发往目标服务器处理
			MessageDispatcher.execute(dispatchUrl,msg, session.getHost());
			return;
		}

		if (session.isConnected()) {

			session.write(msg);
			return;
		}

		// 如果用户标示了APNS ON 说明这是ios设备需要使用apns发送
		if (session.getApnsAble() == CIMSession.APNS_ON) {

			apnsService.push(1, msg, session.getDeviceId());
		}
	}

	/**
	 * 向用户发送消息
	 * 
	 * @param msg
	 */
	public void push(Message msg) {

		push(MessageUtil.transform(msg));
	}

}
