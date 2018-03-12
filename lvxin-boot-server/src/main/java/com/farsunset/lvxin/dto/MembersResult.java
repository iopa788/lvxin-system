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
package com.farsunset.lvxin.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.farsunset.lvxin.model.User;

public class MembersResult {
	public int code;
	public String msg;
	private HashMap<String, Object> data = new HashMap<>();
	private String path;

	public static MembersResult build(String path, User user, List<User> friendList) {
		MembersResult result = new MembersResult();
		result.path = path;
		result.data.put("owner", result.coverToMine(user));
		result.data.put("members", friendList.size());
		result.data.put("list", result.coverToMemberList(friendList));

		return result;
	}

	public class Mine {
		String username;
		String id;
		String status;
		String sign;
		String avatar;
	}

	private List<Mine> coverToMemberList(List<User> friendList) {
		List<Mine> fgList = new ArrayList<Mine>();
		for (User user : friendList) {
			fgList.add(coverToMine(user));
		}
		return fgList;
	}

	private Mine coverToMine(User user) {
		Mine mine = new Mine();
		mine.username = user.getName();
		mine.status = user.getOnline();
		mine.id = user.getAccount();
		mine.sign = user.getMotto();
		mine.avatar = path + "/files/user-icon/" + user.getAccount();
		return mine;
	}
}