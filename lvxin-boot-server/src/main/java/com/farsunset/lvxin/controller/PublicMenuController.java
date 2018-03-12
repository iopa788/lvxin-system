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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.PublicMenu;
import com.farsunset.lvxin.service.PublicMenuService;

@Controller
@RequestMapping("/console/publicMenu")
public class PublicMenuController {

	@Autowired
	private PublicMenuService publicMenuService;

	@RequestMapping(value = "/list.action")
	public @ResponseBody BaseResult list(String account) {
		BaseResult result = new BaseResult();
		result.dataList = publicMenuService.queryList(account);
		return result;
	}

	@RequestMapping(value = "/rootList.action")
	public @ResponseBody BaseResult rootList(String account) {

		BaseResult result = new BaseResult();
		result.dataList = publicMenuService.queryRootList(account);
		return result;

	}

	@RequestMapping(value = "/childList.action")
	public @ResponseBody BaseResult childList(String fid) {

		BaseResult result = new BaseResult();
		result.dataList = publicMenuService.queryChildList(fid);
		return result;
	}

	@RequestMapping(value = "/detailed.action")
	public @ResponseBody BaseResult detailed(String gid) {

		BaseResult result = new BaseResult();
		result.data = publicMenuService.queryById(gid);
		return result;
	}

	@RequestMapping(value = "/update.action")
	public @ResponseBody BaseResult update(PublicMenu publicMenu) {
		BaseResult result = new BaseResult();
		PublicMenu target = publicMenuService.queryById(publicMenu.getGid());
		if (target != null) {
			target.setName(publicMenu.getName());
			target.setLink(publicMenu.getLink());
			target.setType(publicMenu.getType());
			target.setContent(publicMenu.getContent());
			target.setCode(publicMenu.getCode());
			target.setSort(publicMenu.getSort());
			try {

				publicMenuService.update(target);

			} catch (IllegalArgumentException e) {
				result.code = 403;
			}

		} else {
			result.code = 404;
		}

		return result;
	}

	@RequestMapping(value = "/add.action")
	public @ResponseBody BaseResult add(PublicMenu publicMenu) {

		BaseResult result = new BaseResult();
		try {
			publicMenuService.add(publicMenu);
			result.data = publicMenu.getGid();
		} catch (IllegalArgumentException e) {
			result.code = 403;
		}
		return result;
	}

	@RequestMapping(value = "/delete.action")
	public @ResponseBody BaseResult delete(PublicMenu publicMenu) {
		publicMenuService.delete(publicMenu);
		return new BaseResult();
	}
}
