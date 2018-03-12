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
package com.farsunset.lvxin.service;

import java.util.List;

import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.web.jstl.Page;

public interface UserService {

	void update(User user);

	void updateQuietly(User user);

	List<User> getAll();

	User get(String account);

	void save(User user);

	/**
	 * 查询所有在线的并且有坐标的用户
	 * @param account 需要排除的用户
	 * @param gender 性别
	 */
	List<User> queryNearbyList(String account,String gender);

	void queryList(User user, Page page);

	void delete(String account);

	List<String> getAccounts();

	/**
	 * 保存从模板导入的用户信息
	 * @param list
	 * @return
	 */
	int saveBatch(List<User> list);

	/**
	 * 统计部门下有几个成员
	 * @param code
	 * @return
	 */
	int countOrganizationMember(String code);

	
	void updateState(String account, String state);
}
