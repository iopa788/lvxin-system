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
package com.farsunset.lvxin.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.cim.push.DefaultMessagePusher;
import com.farsunset.lvxin.cim.push.MessagePusherDispatcher;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.dto.MessageResult;
import com.farsunset.lvxin.message.bean.Receiver;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.PageCompat;
import com.farsunset.lvxin.util.UUIDTools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/cgi/message")
public class APIMessageController {

	@Autowired
	private MessagePusherDispatcher messagePusherDispatcher;

	@Autowired
	private MessageService messageService;

	@Autowired
	DefaultMessagePusher defaultMessagePusher;

	/**
	 * 此方法仅仅在集群时，通过服务器调用
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "/dispatchSend.api")
	public MessageResult dispatchSend(Message message) {
		return send(message);
	}

	@RequestMapping(value = "/send.api")
	public MessageResult send(Message message) {

		MessageResult result = new MessageResult();

		if (StringUtils.isEmpty(message.getFormat())) {
			message.setFormat(Constants.MessageFormat.FORMAT_TEXT);// 默认为0 文本消息
		}

		messagePusherDispatcher.push(message);

		result.id = message.getMid();
		result.timestamp = message.getTimestamp();

		return result;
	}

	@RequestMapping(value = "/read.api")
	public BaseResult read(Message message, String notify) {

		BaseResult result = new BaseResult();

		String gid = message.getContent();
		messageService.updateStatus(gid, Message.STATUS_READ);

		if ("1".equals(notify)) {

			message.setAction(Constants.MessageAction.ACTION_108);
			defaultMessagePusher.push(message);

		}
		return result;
	}

	@RequestMapping(value = "/received.api")
	public BaseResult received(Message message) {

		BaseResult result = new BaseResult();

		messageService.updateStatus(message.getMid(), Message.STATUS_RECEIVED);

		return result;
	}

	@RequestMapping(value = "/delete.api")
	public BaseResult delete(String mid) {

		BaseResult result = new BaseResult();

		messageService.delete(mid);

		return result;
	}

	@RequestMapping(value = "/list.api")
	public BaseResult list(Message message, @PageNumber int currentPage) {

		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<Message> page = messageService.queryPage(message, pageable);

		return PageCompat.transform(page);
	}

	@RequestMapping(value = "/offlineList.api")
	public BaseResult offlineList(@TokenAccount String account) {

		BaseResult result = new BaseResult();

		if (StringUtils.isEmpty(account)) {
			result.code = 403;
		} else {
			result.dataList = messageService.queryOffLineMessages(account);
		}

		return result;
	}

	@RequestMapping(value = "/batchReceive.api")
	public BaseResult batchReceive(@TokenAccount String account) {

		BaseResult result = new BaseResult();

		messageService.updateBatchReceived(account);

		return result;
	}

	@RequestMapping(value = "/forward.api")
	public BaseResult forward(Message message) {
		BaseResult result = new BaseResult();

		Receiver[] reciverArray = new Gson().fromJson(message.getReceiver(), new TypeToken<Receiver[]>() {
		}.getType());
		List<String> idList = new ArrayList<String>(reciverArray.length);
		for (Receiver reciver : reciverArray) {
			message.setMid(UUIDTools.getUUID());
			message.setReceiver(reciver.id);
			message.setAction(reciver.type);

			messagePusherDispatcher.push(message);

			idList.add(message.getMid());
		}
		result.dataList = idList;

		return result;
	}

	@RequestMapping(value = "/revoke.api")
	public BaseResult revoke(Message message) {

		BaseResult result = new BaseResult();

		message = messageService.queryById(message.getMid());

		if (message == null) {
			result.code = 404;
			return result;
		} 
		
		if (!Message.STATUS_NOT_RECEIVED.equals(message.getStatus())) {
			result.code = 403;
		} else {
			messageService.delete(message.getMid());
		}

		return result;
	}

}
