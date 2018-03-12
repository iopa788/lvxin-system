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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.Article;
import com.farsunset.lvxin.service.ArticleService;
import com.farsunset.lvxin.service.MomentRuleService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.PageCompat;

@RestController
@RequestMapping("/cgi/article")
public class APIArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private MomentRuleService momentRuleService;

	@RequestMapping(value = "/publish.api")
	public BaseResult publish(Article article, @TokenAccount String account) {
		BaseResult result = new BaseResult();

		result.code = 200;
		article.setAccount(account);
		articleService.add(article);

		result.data = article.getGid();

		return result;
	}

	@RequestMapping(value = "/myList.api")
	public BaseResult myList(@TokenAccount String account, @PageNumber int currentPage) {

		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<Article> page = articleService.queryByPage(account, pageable);

		return PageCompat.transform(page);
	}

	@RequestMapping(value = "/list.api")
	public BaseResult list(@TokenAccount String account, String otherAccount, @PageNumber int currentPage) {

		BaseResult result = new BaseResult();

		if (momentRuleService.isBeLimiting(account, otherAccount)) {
			result.code = 403;
			return result;
		} else {

			Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
			Page<Article> page = this.articleService.queryByPage(otherAccount, pageable);
			return PageCompat.transform(page);
		}

	}

	@RequestMapping(value = "/relevantList.api")
	public BaseResult relevantList(@TokenAccount String account, @PageNumber int currentPage) {

		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<Article> page = articleService.queryRelevantByPage(account, pageable);

		return PageCompat.transform(page);
	}

	@RequestMapping(value = "/delete.api")
	public BaseResult delete(String gid) {
		BaseResult result = new BaseResult();

		articleService.delete(gid);

		return result;
	}

	@RequestMapping(value = "/detailed.api")
	public BaseResult detailed(String gid) {
		BaseResult result = new BaseResult();

		result.code = 200;
		result.data = articleService.queryById(gid);

		return result;
	}

}
