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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.cim.push.DefaultMessagePusher;
import com.farsunset.lvxin.cim.push.MessagePusherFactory;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.dto.MessageResult;
import com.farsunset.lvxin.message.bean.Receiver;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.mvc.container.ContextHolder;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.UUIDTools;
import com.farsunset.lvxin.web.jstl.Page;

@RestController
@RequestMapping("/cgi/message")
public class APIMessageController {

	@Autowired
	private MessageService messageServiceImpl;

	/**
	 * 此方法仅仅在集群时，通过服务器调用
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

		try {

			checkParams(message);

			MessagePusherFactory.getFactory().push(message);

			result.id = message.getMid();
			result.timestamp = message.getTimestamp();

		} catch (Exception e) {

			result.code = 500;
			result.message = e.getMessage();
			e.printStackTrace();
		}

		return result;
	}

	@RequestMapping(value = "/read.api")
	public BaseResult read(Message message, String notify) {

		BaseResult result = new BaseResult();

		String gid = message.getContent();
		messageServiceImpl.updateStatus(gid, Message.STATUS_READ);

		if ("1".equals(notify)) {

			message.setAction(Constants.MessageAction.ACTION_108);
			ContextHolder.getBean(DefaultMessagePusher.class).push(message);

		}
		return result;
	}

	@RequestMapping(value = "/received.api")
	public BaseResult received(Message message) {

		BaseResult result = new BaseResult();

		messageServiceImpl.updateStatus(message.getMid(), Message.STATUS_RECEIVED);

		return result;
	}

	@RequestMapping(value = "/delete.api")
	public BaseResult delete(String mid) {

		BaseResult result = new BaseResult();

		messageServiceImpl.delete(mid);

		return result;
	}

	@RequestMapping(value = "/list.api")
	public BaseResult list(Message message, Page page) {

		BaseResult result = new BaseResult();

		this.messageServiceImpl.queryPage(message, page);
		result.dataList = page.getDataList();
		result.page = page.toHashMap();

		return result;
	}

	@RequestMapping(value = "/offlineList.api")
	public BaseResult offlineList(@TokenAccount String account) {

		BaseResult result = new BaseResult();

		if (StringUtils.isEmpty(account)) {
			result.code = 403;
		} else {
			List<Message> list = messageServiceImpl.queryOffLineMessages(account);
			result.dataList = list;
		}

		return result;
	}

	@RequestMapping(value = "/batchReceive.api")
	public BaseResult batchReceive(@TokenAccount String account) {

		BaseResult result = new BaseResult();

		messageServiceImpl.updateBatchReceived(account);

		return result;
	}

	@RequestMapping(value = "/forward.api")
	public BaseResult forward(Message message) {
		BaseResult result = new BaseResult();

		List<Receiver> reciverList = JSON.parseObject(message.getReceiver(), new TypeReference<List<Receiver>>() {
		}.getType());
		List<String> idList = new ArrayList<String>(reciverList.size());
		for (Receiver reciver : reciverList) {
			message.setMid(UUIDTools.getUUID());
			message.setReceiver(reciver.id);
			message.setAction(reciver.type);

			MessagePusherFactory.getFactory().push(message);

			idList.add(message.getMid());
		}
		result.dataList = idList;

		return result;
	}

	@RequestMapping(value = "/revoke.api")
	public BaseResult revoke(Message message) {

		BaseResult result = new BaseResult();

		message = messageServiceImpl.queryById(message.getMid());

		if (message == null) {
			result.code = 404;
		} else {
			if (!Message.STATUS_NOT_RECEIVED.equals(message.getStatus())) {
				result.code = 403;
			} else {
				messageServiceImpl.delete(message.getMid());
			}

		}

		return result;
	}

	private void checkParams(Message message) {

		if (StringUtils.isEmpty(message.getReceiver())) {
			throw new IllegalArgumentException("receiver 不能为空!");

		}
		if (StringUtils.isEmpty(message.getFormat())) {
			message.setFormat(Constants.MessageFormat.FORMAT_TEXT);// 默认为0 文本消息
		}
	}

}
