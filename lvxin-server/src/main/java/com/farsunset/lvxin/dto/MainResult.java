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

import com.alibaba.fastjson.annotation.JSONField;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.User;

public class MainResult {
	public int code;
	public String msg;
	public HashMap<String, Object> data = new HashMap<>();
	
	@JSONField(serialize=false)
	public String path;

	public static MainResult build(String path,User user, List<User> friendList, List<Group> groupList) {
		MainResult result = new MainResult();
		result.path = path;
		result.data.put("mine", result.coverToMine(user));
		result.data.put("friend", result.coverToFriendGroupList(user,friendList));
		result.data.put("group", result.coverToLayimGroupList(groupList));
		return result;
	}

	public  class Mine {
		public String username;
		public String id;
		public String status;
		public String sign;
		public String avatar;
	}
	
	public  class LayimGroup {
		public String groupname;
		public String id;
		public String avatar;
	}
	public  class FriendGroup {
		public String groupname;
		public String id = "1";
		public int online;
		public List<Mine> list = new ArrayList<>();
	}
	public  List<LayimGroup> coverToLayimGroupList( List<Group> groupList) {
		List<LayimGroup> fgList = new  ArrayList<LayimGroup>();
		for(Group group:groupList) {
			fgList.add(coverToLayimGroup(group));
		}
		return fgList;
	}
	public  List<FriendGroup> coverToFriendGroupList(User user,List<User> friendList) {
		List<FriendGroup> fgList = new  ArrayList<FriendGroup>();
		FriendGroup group = new FriendGroup();
		group.groupname="通讯录";
		for(User friend:friendList) {
			if(!friend.getAccount().equals(user.getAccount())) {
				group.list.add(coverToMine(friend));
				if(User.ON_LINE.equals(friend.getOnline())) {
					group.online++;
				}
			}
		}
		fgList.add(group);
		return fgList;
	}
	
	public  LayimGroup coverToLayimGroup(Group group) {
		LayimGroup mine = new LayimGroup();
		mine.groupname = group.getName();
		mine.id = group.getGroupId().toString();
		mine.avatar = path+"/files/group-icon/"+group.getGroupId();
		return mine;
	}
	public  Mine coverToMine(User user) {
		Mine mine = new Mine();
		mine.username = user.getName();
		mine.status = User.OFF_LINE.equals(user.getOnline()) ? "offline":"online";
		mine.id = user.getAccount();
		mine.sign = user.getMotto();
		mine.avatar = path+"/files/user-icon/"+user.getAccount();
		return mine;
	}
}