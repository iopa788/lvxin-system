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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.exception.IllegalExistCodeException;
import com.farsunset.lvxin.exception.IllegalNullArgumentException;
import com.farsunset.lvxin.model.Config;
import com.farsunset.lvxin.service.ConfigService;
import com.farsunset.lvxin.util.Constants;

@Controller
@RequestMapping("/console/config")
public class ConfigController {

	@Autowired
	private ConfigService configService;

	@RequestMapping(value = "/list.action")
	public String list(Config config, @PageNumber int currentPage, Model model) {
		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<Config> page = configService.queryPage(config, pageable);
		model.addAttribute("page", page);
		model.addAttribute("config", config);
		return "console/config/manage";
	}

	@RequestMapping(value = "/update.action")
	public @ResponseBody BaseResult update(Config config) {
		BaseResult result = new BaseResult();

		try {
			configService.update(config);
		} catch (IllegalExistCodeException e) {
			result.code = 401;
		}
		return result;
	}

	@RequestMapping(value = "/save.action")
	public @ResponseBody BaseResult save(Config config) {
		BaseResult result = new BaseResult();
		try {
			configService.save(config);
		} catch (IllegalExistCodeException e) {
			result.code = 401;
		} catch (IllegalNullArgumentException e) {
			result.code = 403;
		}
		return result;
	}

	@RequestMapping(value = "/delete.action")
	public @ResponseBody BaseResult delete(Config config) {
		configService.delete(config.getGid());
		return new BaseResult();
	}

	@RequestMapping(value = "/saveVersion.action")
	public @ResponseBody BaseResult saveVersion(HttpServletRequest request, HttpServletResponse response) {
		BaseResult result = new BaseResult();
		String versionCode = request.getParameter("versionCode");
		String versionName = request.getParameter("versionName");
		String url = request.getParameter("url");
		String description = request.getParameter("description");
		Config config = new Config();
		config.setDomain(request.getParameter("domain"));
		try {
			config.setKey("versionCode");
			config.setValue(versionCode);
			configService.save(config);

			config.setKey("versionName");
			config.setValue(versionName);
			configService.save(config);

			config.setKey("url");
			config.setValue(url);
			configService.save(config);

			config.setKey("description");
			config.setValue(description);
			configService.save(config);

		} catch (IllegalExistCodeException e) {
			result.code = 401;
		} catch (IllegalNullArgumentException e) {
			result.code = 403;
		}
		return result;
	}

}
