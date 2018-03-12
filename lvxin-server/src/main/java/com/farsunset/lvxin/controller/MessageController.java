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

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farsunset.lvxin.cim.push.BroadcastMessagePusher;
import com.farsunset.lvxin.cim.push.MessagePusherFactory;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.web.jstl.Page;

@Controller
@RequestMapping("/console/message")
public class MessageController {

	String containedTypes = "0,2,3,201";
	@Autowired
	private MessageService messageServiceImpl;
	@Autowired
	BroadcastMessagePusher broadcastMessagePusher;

	@RequestMapping(value = "/list.action")
	public ModelAndView list(Message message, Page page) {

		if (StringUtils.isBlank(message.getAction())) {
			message.setAction(containedTypes);
		}
		messageServiceImpl.queryPage(message, page);

		ModelAndView model = new ModelAndView();
		model.setViewName("message/manage");
		return model;

	}

	@RequestMapping(value = "/delete.action")
	public @ResponseBody HashMap<String, Object> delete(@RequestParam(value = "mid") String mid) throws Exception {

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);
		messageServiceImpl.delete(mid);
		return datamap;
	}

	@RequestMapping(value = "/broadcast.action")
	public @ResponseBody HashMap<String, Object> broadcast(Message message) {

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);
		broadcastMessagePusher.push(message);
		return datamap;
	}

	@RequestMapping(value = "/broadcastOnline.action")
	public @ResponseBody HashMap<String, Object> broadcastOnline(Message message) {

		HashMap<String, Object> datamap = new HashMap<String, Object>();

		datamap.put("code", 200);

		broadcastMessagePusher.pushOnline(message);
		return datamap;
	}

	@RequestMapping(value = "/send.action")
	public @ResponseBody HashMap<String, Object> send(Message message) {

		HashMap<String, Object> datamap = new HashMap<String, Object>();

		datamap.put("code", 200);

		try {
			MessagePusherFactory.getFactory().push(message);
		} catch (Exception e) {

			datamap.put("code", 500);
			e.printStackTrace();
		}

		return datamap;
	}

}
