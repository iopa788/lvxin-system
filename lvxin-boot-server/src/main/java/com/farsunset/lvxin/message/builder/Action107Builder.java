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
package com.farsunset.lvxin.message.builder;

import java.util.List;

import com.farsunset.lvxin.model.Group;
import com.google.gson.Gson;

public class Action107Builder extends BaseBuilder {

	private List<String> accountList;
	private long groupId;
	private String groupName;

	public String buildJsonString(Group group, List<String> list) {
		groupId = group.getGroupId();
		groupName = group.getName();
		accountList = list;
		return new Gson().toJson(this);
	}
}
