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

import com.alibaba.fastjson.JSON;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.User;

public class Action112Builder extends BaseBuilder {

	public String acount;
	public String name;
	public long groupId;
	public String groupName;

	public String buildJsonString(User self, Group group) {
		acount = self.getAccount();
		name = self.getName();
		groupId = group.getGroupId();
		groupName = group.getName();
		return JSON.toJSONString(this);
	}
}
