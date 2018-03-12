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
package com.farsunset.lvxin.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.cim.push.MessagePusherFactory;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.dto.MainResult;
import com.farsunset.lvxin.dto.MembersResult;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.GroupMemberService;
import com.farsunset.lvxin.service.GroupService;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.service.UserService;

@RestController
@RequestMapping("/web/client")
public class WebClientController {

	@Autowired
	private UserService userService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private GroupMemberService groupMemberService;
	@Autowired
	private MessageService messageService;
	@RequestMapping(value = "/send.api")
	public @ResponseBody BaseResult send(Message message) {
		BaseResult result = new BaseResult();
		MessagePusherFactory.getFactory().push(message);
		return result;
	}

	@RequestMapping(value = "/receipt.api")
	public @ResponseBody BaseResult receipt(String mid) {
		BaseResult result = new BaseResult();
		messageService.updateStatus(mid, Message.STATUS_RECEIVED);
		return result;
	}
	
	@RequestMapping(value = "/login.api")
	public BaseResult login(User user) {

		String password = DigestUtils.md5Hex(user.getPassword());
		BaseResult result = new BaseResult();
		user = userService.get(user.getAccount());
		 
		if (user == null ||!password.equalsIgnoreCase(user.getPassword())) {
			result.code = 403;
			return result;
		}
		
		if (user.isDisabled()) {
			result.code = 405;
			return result;
		}
		return result;
	}
	
	@RequestMapping(value = "/members.api")
	public MembersResult members(HttpServletRequest request,Long id) {
		List<User> meberList = groupMemberService.getUserList(id);
		User user = groupService.queryFounder(id);
		String path = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
		return MembersResult.build(path,user,meberList);
	}

	
	@RequestMapping(value = "/main.api")
	public MainResult main(HttpServletRequest request,String account) {
		User user = userService.get(account);
		List<User> friendList = userService.getAll();
		List<Group> groupList = groupService.getRelevantList(account);
		String path = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
		return MainResult.build(path,user, friendList, groupList);
	}

}
