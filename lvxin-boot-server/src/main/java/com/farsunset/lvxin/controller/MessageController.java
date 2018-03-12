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
package com.farsunset.lvxin.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.cim.push.BroadcastMessagePusher;
import com.farsunset.lvxin.cim.push.MessagePusherDispatcher;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.util.Constants;

@Controller
@RequestMapping("/console/message")
public class MessageController {

	private String containedTypes = "0,2,3,201";
	@Autowired
	private MessageService messageService;
	@Autowired
	private MessagePusherDispatcher messagePusherDispatcher;
	@Autowired
	BroadcastMessagePusher broadcastMessagePusher;

	@RequestMapping(value = "/list.action")
	public String list(Message message, @PageNumber int currentPage, Model model) {
		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		if (StringUtils.isBlank(message.getAction())) {
			message.setAction(containedTypes);
		}
		Page<Message> page = messageService.queryPage(message, pageable);
		model.addAttribute("page", page);
		model.addAttribute("message", message);
		return "console/message/manage";

	}

	@RequestMapping(value = "/delete.action")
	public @ResponseBody BaseResult delete(String mid){

		messageService.delete(mid);
		return new BaseResult();
	}

	@RequestMapping(value = "/get.action")
	public @ResponseBody BaseResult get(String mid){
		BaseResult result = new BaseResult();
		result.data = messageService.queryById(mid);
		return result;
	}

	
	@RequestMapping(value = "/broadcast.action")
	public @ResponseBody BaseResult broadcast(Message message) {

		broadcastMessagePusher.push(message);
		return new BaseResult();
	}

	@RequestMapping(value = "/broadcastOnline.action")
	public @ResponseBody BaseResult broadcastOnline(Message message) {

		broadcastMessagePusher.pushOnline(message);
		return new BaseResult();
	}

	@RequestMapping(value = "/send.action")
	public @ResponseBody BaseResult send(Message message) {

		messagePusherDispatcher.push(message);
		return new BaseResult();
	}

}
