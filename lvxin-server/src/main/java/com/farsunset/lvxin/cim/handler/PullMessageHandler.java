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

import java.util.List;

import org.apache.log4j.Logger;

import com.farsunset.cim.sdk.server.constant.CIMConstant;
import com.farsunset.cim.sdk.server.handler.CIMRequestHandler;
import com.farsunset.cim.sdk.server.model.ReplyBody;
import com.farsunset.cim.sdk.server.model.SentBody;
import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.mvc.container.ContextHolder;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.service.impl.MessageServiceImpl;
import com.farsunset.lvxin.util.MessageUtil;

/**
 * 推送离线消息 建议登录 使用http 后接口拉去离线消息,因为可能离线消息过多，不适用于逐条推送
 */
@Deprecated
public class PullMessageHandler implements CIMRequestHandler {

	protected final Logger logger = Logger.getLogger(PullMessageHandler.class);

	@Override
	public ReplyBody process(CIMSession ios, SentBody message) {

		ReplyBody reply = new ReplyBody();
		reply.setCode(CIMConstant.ReturnCode.CODE_200);
		try {
			MessageService messageService = ContextHolder.getBean(MessageServiceImpl.class);
			String account = message.get("account");
			List<Message> list = messageService.queryOffLineMessages(account);
			for (Message m : list) {

				ios.write(MessageUtil.transform(m));
			}

		} catch (Exception e) {
			reply.setCode(CIMConstant.ReturnCode.CODE_500);
			e.printStackTrace();
			logger.error("拉取离线消息失败", e);
		}
		return reply;
	}
}
