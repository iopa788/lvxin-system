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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.service.GroupService;

@RestController
@RequestMapping("/cgi/group")
public class APIGroupController {

	@Autowired
	private GroupService groupService;

	@RequestMapping(value = "/list.api")
	public BaseResult list(@TokenAccount String account) {

		BaseResult result = new BaseResult();

		result.dataList = groupService.getRelevantList(account);

		return result;
	}

	@RequestMapping(value = "/look.api")
	public BaseResult look() {

		BaseResult result = new BaseResult();
		result.dataList = groupService.queryAll();

		return result;
	}

	@RequestMapping(value = "/detailed.api")
	public BaseResult detailed(long groupId) {

		BaseResult result = new BaseResult();

		result.data = groupService.queryById(groupId);

		return result;
	}

	@RequestMapping(value = "/create.api")
	public BaseResult create(Group group, @TokenAccount String founder) {

		BaseResult result = new BaseResult();
		group.setFounder(founder);
		groupService.add(group);

		result.data = group;

		return result;
	}

	@RequestMapping(value = "/update.api")
	public BaseResult update(Group group) {

		BaseResult result = new BaseResult();

		Group target = groupService.queryById(group.getGroupId());
		target.setName(group.getName());
		target.setSummary(group.getSummary());
		groupService.update(target);

		return result;
	}

	@RequestMapping(value = "/disband.api")
	public BaseResult disband(Group group, @TokenAccount String founder) {
		BaseResult result = new BaseResult();
		group.setFounder(founder);
		groupService.delete(group.getFounder(), group.getGroupId());
		return result;
	}

}
