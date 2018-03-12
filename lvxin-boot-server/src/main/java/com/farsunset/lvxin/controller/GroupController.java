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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.GroupMemberService;
import com.farsunset.lvxin.service.GroupService;
import com.farsunset.lvxin.util.Constants;

@Controller
@RequestMapping("/console/group")
public class GroupController {
	@Autowired
	private GroupService groupService;
	@Autowired
	private GroupMemberService groupMemberService;

	@RequestMapping(value = "/list.action")
	public String list(Group group, @PageNumber int currentPage, Model model) {

		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<Group> page = groupService.queryPage(group, pageable);
		model.addAttribute("page", page);
		model.addAttribute("group", group);
		return "console/group/manage";

	}

	@RequestMapping(value = "/memberList.action")
	public @ResponseBody List<User> memberList(Long groupId) {
		return groupMemberService.getUserList(groupId);
	}
}
