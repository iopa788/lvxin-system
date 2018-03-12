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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farsunset.cim.sdk.server.constant.CIMConstant;
import com.farsunset.cim.sdk.server.handler.CIMRequestHandler;
import com.farsunset.cim.sdk.server.model.ReplyBody;
import com.farsunset.cim.sdk.server.model.SentBody;
import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.cim.push.BroadcastMessagePusher;
import com.farsunset.lvxin.message.builder.Action111Builder;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.util.Constants;

/**
 * 
 * 当客户端修改签名或者名称时，发送通知给其他用户，更新该用户的信息
 *
 */
@Component
public class ModifyProfileHandler implements CIMRequestHandler {

	@Autowired
	private MessageService messageService;
	@Autowired
	private BroadcastMessagePusher broadcastMessagePusher;
	@Autowired
	private UserService userService;

	@Override
	public ReplyBody process(CIMSession ios, SentBody message) {

		ReplyBody reply = new ReplyBody();
		reply.setCode(CIMConstant.ReturnCode.CODE_200);

		String account = message.get("account");
		String motto = message.get("motto");

		User target = userService.get(account);
		target.setMotto(motto);
		userService.updateQuietly(target);

		// 删除掉未接收的更新信息通知
		messageService.deleteBySenderAndAction(account, Constants.MessageAction.ACTION_111);

		sendBroadcast(target);

		return reply;

	}

	private void sendBroadcast(User user) {
		Message updateMessage = new Message();
		updateMessage.setAction(Constants.MessageAction.ACTION_111);
		updateMessage.setContent(new Action111Builder().buildJsonString(user.getAccount(), user.getName(), user.getMotto()));
		updateMessage.setSender(user.getAccount());
		updateMessage.setFormat(Constants.MessageFormat.FORMAT_TEXT);
		broadcastMessagePusher.push(updateMessage);
	}

}
