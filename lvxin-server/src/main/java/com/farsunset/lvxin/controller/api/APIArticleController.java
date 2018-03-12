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

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.Article;
import com.farsunset.lvxin.service.ArticleService;
import com.farsunset.lvxin.service.MomentRuleService;
import com.farsunset.lvxin.web.jstl.Page;

@RestController
@RequestMapping("/cgi/article")
public class APIArticleController {

	@Autowired
	private ArticleService articleServiceImpl;

	@Autowired
	private MomentRuleService momentRuleServiceImpl;

	@RequestMapping(value = "/publish.api")
	public BaseResult publish(Article article,@TokenAccount String account) throws IOException {
		BaseResult result = new BaseResult();

		result.code = 200;
		article.setAccount(account);
		articleServiceImpl.add(article);

		result.data = article.getGid();

		return result;
	}

	@RequestMapping(value = "/myList.api")
	public BaseResult myList(@TokenAccount String account, Page page) {

		BaseResult result = new BaseResult();

		page = this.articleServiceImpl.queryByPage(account, page);
		result.dataList = page.getDataList();
		result.page = page.toHashMap();

		return result;
	}

	@RequestMapping(value = "/list.api")
	public BaseResult list(@TokenAccount String account, String otherAccount, Page page) {

		BaseResult result = new BaseResult();

		if (momentRuleServiceImpl.isBeLimiting(account, otherAccount)) {
			result.code = 403;
		} else {
			page = this.articleServiceImpl.queryByPage(otherAccount, page);
			result.dataList = page.getDataList();
			result.page = page.toHashMap();
		}

		return result;
	}

	@RequestMapping(value = "/relevantList.api")
	public BaseResult relevantList(@TokenAccount String account, Page page) {

		BaseResult result = new BaseResult();

		this.articleServiceImpl.queryRelevantByPage(account, page);

		result.dataList = page.getDataList();
		result.page = page.toHashMap();

		return result;
	}

	@RequestMapping(value = "/delete.api")
	public BaseResult delete(String gid) {
		BaseResult result = new BaseResult();

		articleServiceImpl.delete(gid);

		return result;
	}

	@RequestMapping(value = "/detailed.api")
	public BaseResult detailed(String gid) throws IOException {
		BaseResult result = new BaseResult();

		result.code = 200;
		result.data = articleServiceImpl.queryById(gid);

		return result;
	}

}
