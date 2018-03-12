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
package com.farsunset.lvxin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.model.Article;
import com.farsunset.lvxin.service.ArticleService;
import com.farsunset.lvxin.util.Constants;

@Controller
@RequestMapping("/console/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@RequestMapping(value = "/list.action")
	public String list(Article article, @PageNumber int currentPage, Model model) {
		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<Article> page = articleService.queryByPage(article, pageable);
		model.addAttribute("page", page);
		model.addAttribute("article", article);
		return "console/article/manage";
	}

	@RequestMapping(value = "/detailed.action")
	public @ResponseBody Article detailed(Article article) {

		return articleService.queryById(article.getGid());
	}

}
