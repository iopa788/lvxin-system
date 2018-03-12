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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.farsunset.cim.sdk.server.constant.CIMConstant;
import com.farsunset.cim.sdk.server.handler.CIMRequestHandler;
import com.farsunset.cim.sdk.server.model.ReplyBody;
import com.farsunset.cim.sdk.server.model.SentBody;
import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.cim.push.BroadcastMessagePusher;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.util.Constants;

/**
 * 当客户端修改头像时，发送通知给其他用户，更新该用户的头像
 */
public class ModifyLogoHandler implements CIMRequestHandler {

	protected final Logger logger = Logger.getLogger(ModifyLogoHandler.class);
	@Autowired
	private MessageService messageService;
	@Autowired
	private BroadcastMessagePusher broadcastMessagePusher;

	@Override
	public ReplyBody process(CIMSession ios, SentBody message) {

		ReplyBody reply = new ReplyBody();
		reply.setCode(CIMConstant.ReturnCode.CODE_200);
		String account = message.get("account");

		// 删除掉其他用户未接收的头像更新通知
		messageService.deleteBySenderAndAction(account, Constants.MessageAction.ACTION_110);

		sendBroadcast(account);

		return reply;

	}

	private void sendBroadcast(String account) {
		Message updateMessage = new Message();
		updateMessage.setAction(Constants.MessageAction.ACTION_110);
		updateMessage.setContent(account);
		updateMessage.setSender(account);
		broadcastMessagePusher.push(updateMessage);
	}

}
