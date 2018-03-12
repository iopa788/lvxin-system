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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farsunset.lvxin.model.Host;
import com.farsunset.lvxin.service.HostService;

@Controller
@RequestMapping("/console/host")
public class HostController {

	@Autowired
	private HostService hostServiceImpl;

	@RequestMapping(value = "/list.action")
	public ModelAndView list() {

		ModelAndView model = new ModelAndView();
		model.addObject("list", hostServiceImpl.queryList());
		model.setViewName("host/manage");
		return model;
	}

	@RequestMapping(value = "/add.action")
	public @ResponseBody HashMap<String, Object> add(Host host) {

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		try {
			datamap.put("code", 200);
			hostServiceImpl.save(host);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datamap;
	}

	@RequestMapping(value = "/delete.action")
	public @ResponseBody HashMap<String, Object> delete(@RequestParam(value = "ip") String ip) throws IOException {

		HashMap<String, Object> datamap = new HashMap<String, Object>();

		datamap.put("code", 200);
		hostServiceImpl.delete(ip);
		return datamap;
	}

}
