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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.GroupMemberService;
import com.farsunset.lvxin.service.GroupService;
import com.farsunset.lvxin.web.jstl.Page;

@Controller
@RequestMapping("/console/group")
public class GroupController {
	@Autowired
	private GroupService groupServiceImpl;
	@Autowired
	private GroupMemberService groupMemberServiceImpl;

	@RequestMapping(value = "/list.action")
	public ModelAndView list(@ModelAttribute("group") Group group, @ModelAttribute("page") Page page) {

		ModelAndView model = new ModelAndView();
		groupServiceImpl.queryPage(group, page);
		model.addObject("page", page);
		model.setViewName("group/manage");
		return model;
	}

	@RequestMapping(value = "/memberList.action")
	public @ResponseBody List<User> memberList(@RequestParam(value = "groupId") Long groupId) {
		return groupMemberServiceImpl.getUserList(groupId);
	}
}
