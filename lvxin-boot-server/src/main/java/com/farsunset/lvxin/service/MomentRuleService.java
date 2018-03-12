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

import com.farsunset.lvxin.model.MomentRule;

public interface MomentRuleService {

	void save(MomentRule model);

	void delete(MomentRule model);

	/***
	 * 查询我是否被对方限制访问TA的空间
	 * 
	 * @param account
	 *            我的账号
	 * @param otherAccount
	 *            对方账号
	 * @return true 被限制的，false 没有被限制
	 */
	boolean isBeLimiting(String account, String otherAccount);

	/**
	 * 获取被我屏蔽了的用户列表
	 * 
	 * @param account
	 *            我的账号
	 * @return
	 */
	List<String> queryMeLimitingList(String account);

	/**
	 * 获取屏蔽了我的用户列表
	 * 
	 * @param account
	 *            我的账号
	 * @return
	 */
	List<String> queryLimitingMeList(String account);

	/***
	 * 查询我设置过的朋友圈权限列表
	 * 
	 * @param account
	 *            我的账号
	 * @return
	 */
	List<MomentRule> queryList(String account);

	/**
	 * 查询出所有已经过滤过(排除 > 我屏蔽了的+屏蔽了我的+我自己)的用户账号
	 * 
	 * @param account
	 *            我的账号
	 * @return
	 */
	List<String> queryFilteredList(String account);

}
