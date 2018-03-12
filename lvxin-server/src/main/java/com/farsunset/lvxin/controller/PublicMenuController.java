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

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.farsunset.lvxin.model.PublicMenu;
import com.farsunset.lvxin.service.PublicMenuService;

@Controller
@RequestMapping("/console/publicMenu")
public class PublicMenuController {

	@Autowired
	private PublicMenuService publicMenuServiceImpl;

	@RequestMapping(value = "/list.action")
	public @ResponseBody HashMap<String, Object> list(@RequestParam(value = "account") String account) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", 200);
		map.put("dataList", publicMenuServiceImpl.queryList(account));
		return map;
	}

	@RequestMapping(value = "/rootList.action")
	public @ResponseBody HashMap<String, Object> rootList(@RequestParam(value = "account") String account) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", 200);
		map.put("dataList", publicMenuServiceImpl.queryRootList(account));
		return map;
	}

	@RequestMapping(value = "/childList.action")
	public @ResponseBody HashMap<String, Object> childList(@RequestParam(value = "fid") String fid) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", 200);
		map.put("dataList", publicMenuServiceImpl.queryChildList(fid));
		return map;
	}

	@RequestMapping(value = "/detailed.action")
	public @ResponseBody HashMap<String, Object> detailed(@RequestParam(value = "gid") String gid) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", 200);
		map.put("data", publicMenuServiceImpl.queryById(gid));
		return map;
	}

	@RequestMapping(value = "/update.action")
	public @ResponseBody HashMap<String, Object> update(PublicMenu publicMenu) {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);
		PublicMenu target = publicMenuServiceImpl.queryById(publicMenu.getGid());
		if (target != null) {
			target.setName(publicMenu.getName());
			target.setLink(publicMenu.getLink());
			target.setType(publicMenu.getType());
			target.setContent(publicMenu.getContent());
			target.setCode(publicMenu.getCode());
			target.setSort(publicMenu.getSort());
			try {

				publicMenuServiceImpl.update(target);

			} catch (IllegalArgumentException e) {
				datamap.put("code", 403);
			}

		} else {
			datamap.put("code", 404);
		}

		return datamap;
	}

	@RequestMapping(value = "/add.action")
	public @ResponseBody HashMap<String, Object> add(PublicMenu publicMenu) {

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);
		try {
			publicMenuServiceImpl.add(publicMenu);
			datamap.put("data", publicMenu.getGid());
		} catch (IllegalArgumentException e) {
			datamap.put("code", 403);
		}
		return datamap;
	}

	@RequestMapping(value = "/delete.action")
	public @ResponseBody HashMap<String, Object> delete(PublicMenu publicMenu) {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);
		publicMenuServiceImpl.delete(publicMenu);
		return datamap;
	}
}
