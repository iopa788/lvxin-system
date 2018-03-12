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

import com.farsunset.lvxin.model.Article;
import com.farsunset.lvxin.web.jstl.Page;

public interface ArticleService {
	void add(Article article);

	void delete(String articleId);

	void deleteBatch(String account);

	/**
	 * 查询空间时间线列表
	 * 
	 * @param account
	 *            用户账户，用于 过滤空间访问限制相关逻辑
	 * @param page
	 *            分页信息
	 * @return
	 */
	Page queryRelevantByPage(String account, Page page);

	/**
	 * 查询某个用户的空间记录
	 * 
	 * @param account
	 *            用户账户
	 * @param page
	 *            分页信息
	 * @return
	 */
	Page queryByPage(String account, Page page);

	/**
	 * 后台管理页面查询空间记录
	 * 
	 * @param article
	 *            条件信息
	 * @param page
	 *            分页信息
	 * @return
	 */
	Page queryByPage(Article article, Page page);

	Article queryById(String gid);
}
