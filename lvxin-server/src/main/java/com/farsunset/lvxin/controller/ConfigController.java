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

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farsunset.lvxin.exception.IllegalExistCodeException;
import com.farsunset.lvxin.exception.IllegalNullArgumentException;
import com.farsunset.lvxin.model.Config;
import com.farsunset.lvxin.service.ConfigService;

@Controller
@RequestMapping("/console/config")
public class ConfigController {

	@Autowired
	private ConfigService configServiceImpl;

	@RequestMapping(value = "/list.action")
	public ModelAndView list(@ModelAttribute("config") Config config) {
		ModelAndView model = new ModelAndView();
		model.addObject("list", configServiceImpl.queryList(config));
		model.setViewName("config/manage");
		return model;
	}

	@RequestMapping(value = "/update.action")
	public @ResponseBody HashMap<String, Object> update(Config config) throws IOException {
		HashMap<String, Object> datamap = new HashMap<String, Object>();

		datamap.put("code", 200);
		try {
			configServiceImpl.update(config);
		} catch (IllegalExistCodeException e) {
			datamap.put("code", 401);
		}
		return datamap;
	}

	@RequestMapping(value = "/save.action")
	public @ResponseBody HashMap<String, Object> save(Config config) throws IOException {
		HashMap<String, Object> datamap = new HashMap<String, Object>();

		datamap.put("code", 200);
		try {
			configServiceImpl.save(config);
		} catch (IllegalExistCodeException e) {
			datamap.put("code", 401);
		} catch (IllegalNullArgumentException e) {
			datamap.put("code", 403);
		}
		return datamap;
	}

	@RequestMapping(value = "/delete.action")
	public @ResponseBody HashMap<String, Object> delete(Config config) throws IOException {
		HashMap<String, Object> datamap = new HashMap<String, Object>();

		datamap.put("code", 200);
		configServiceImpl.delete(config.getGid());
		return datamap;
	}

	@RequestMapping(value = "/saveVersion.action")
	public @ResponseBody HashMap<String, Object> saveVersion(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HashMap<String, Object> datamap = new HashMap<String, Object>();

		datamap.put("code", 200);
		String versionCode = request.getParameter("versionCode");
		String versionName = request.getParameter("versionName");
		String url = request.getParameter("url");
		String description = request.getParameter("description");
		Config config = new Config();
		config.setDomain(request.getParameter("domain"));
		try {
			config.setKey("versionCode");
			config.setValue(versionCode);
			configServiceImpl.save(config);

			config.setKey("versionName");
			config.setValue(versionName);
			configServiceImpl.save(config);

			config.setKey("url");
			config.setValue(url);
			configServiceImpl.save(config);

			config.setKey("description");
			config.setValue(description);
			configServiceImpl.save(config);

		} catch (IllegalExistCodeException e) {
			datamap.put("code", 401);
		} catch (IllegalNullArgumentException e) {
			datamap.put("code", 403);
		}
		return datamap;
	}

}
