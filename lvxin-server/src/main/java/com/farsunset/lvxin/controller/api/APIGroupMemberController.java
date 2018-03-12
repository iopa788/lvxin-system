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
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.cim.push.BroadcastMessagePusher;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.service.GroupMemberService;
import com.farsunset.lvxin.service.GroupService;
import com.farsunset.lvxin.util.Constants;

@RestController
@RequestMapping("/cgi/groupMember")
public class APIGroupMemberController {

	@Autowired
	private GroupMemberService groupMemberServiceImpl;

	@Autowired
	private GroupService groupServiceImpl;

	@Autowired
	BroadcastMessagePusher broadcastMessagePusher;

	@RequestMapping(value = "/list.api")
	public BaseResult list(long groupId) {

		BaseResult result = new BaseResult();

		result.dataList = groupMemberServiceImpl.getMemberList(groupId);
		return result;
	}

	@RequestMapping(value = "/add.api")
	public BaseResult add(GroupMember groupMember) {

		BaseResult result = new BaseResult();

		if (groupServiceImpl.queryById(groupMember.getGroupId()) == null) {
			result.code = 404;
		} else {
			GroupMember member = new GroupMember();
			member.setAccount(groupMember.getAccount());
			member.setGroupId(groupMember.getGroupId());
			member.setHost(GroupMember.RULE_NORMAL);
			groupMemberServiceImpl.add(member);
			result.data = member.getGid();
		}

		return result;
	}

	@RequestMapping(value = "/remove.api")
	@Deprecated
	/**
	 *Use {@link #quit(GroupMember, String)} instead.
	 */
	public BaseResult remove(GroupMember groupMember,@TokenAccount String account) {

		BaseResult result = new BaseResult();
		groupMember.setAccount(account);
		groupMemberServiceImpl.remove(groupMember);
		return result;
	}

	@RequestMapping(value = "/quit.api")
	public BaseResult quit(GroupMember groupMember,@TokenAccount String account) {
		BaseResult result = new BaseResult();
		groupMember.setAccount(account);
		groupMemberServiceImpl.remove(groupMember);
		return result;
	}
	
	@RequestMapping(value = "/getout.api")
	public BaseResult getout(Long groupId, String account) {

		BaseResult result = new BaseResult();

		List<String> list = new ArrayList<String>();
		String[] accounts = account.split(",");
		list.addAll(Arrays.asList(accounts));

		groupMemberServiceImpl.remove(list,groupId);

		return result;

	}

	@RequestMapping(value = "/invite.api")
	public BaseResult invite(String account, String content) {

		BaseResult result = new BaseResult();

		String[] accounts = account.split(",");

		broadcastMessage(Arrays.asList(accounts), content);

		return result;

	}

	private void broadcastMessage(final List<String> list, String content) {
		Message message = new Message();
		message.setAction(Constants.MessageAction.ACTION_105);
		message.setContent(content);
		message.setSender(Constants.SYSTEM);

		broadcastMessagePusher.push(list, message);

	}

}
