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
package com.farsunset.lvxin.controller.mgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.exception.IllegalExceedRootLimitException;
import com.farsunset.lvxin.exception.IllegalExceedSubLimitException;
import com.farsunset.lvxin.exception.IllegalExistCodeException;
import com.farsunset.lvxin.model.PublicMenu;
import com.farsunset.lvxin.service.PublicMenuService;

@RestController
@RequestMapping("/mgr/publicMenu")
public class MgrPublicMenuController {

	@Autowired
	private PublicMenuService publicMenuServiceImpl;

	@RequestMapping(value = "/list.api")
	public BaseResult list(String account) {
		BaseResult result = new BaseResult();
		result.dataList = publicMenuServiceImpl.queryList(account);
		return result;
	}

	@RequestMapping(value = "/detailed.api")
	public BaseResult detailed(String gid) {
		BaseResult result = new BaseResult();
		result.data = publicMenuServiceImpl.queryById(gid);
		return result;
	}

	@RequestMapping(value = "/update.api")
	public BaseResult update(PublicMenu publicMenu) {
		BaseResult result = new BaseResult();

		PublicMenu target = publicMenuServiceImpl.queryById(publicMenu.getGid());
		if (target != null) {
			target.setName(publicMenu.getName());
			target.setLink(publicMenu.getLink());
			target.setType(publicMenu.getType());
			target.setContent(publicMenu.getContent());
			target.setCode(publicMenu.getCode());

			try {
				publicMenuServiceImpl.update(target);
			} catch (IllegalExistCodeException e) {
				result.code = 401;
			}
		} else {
			result.code = 404;
		}

		return result;
	}

	@RequestMapping(value = "/add.api")
	public BaseResult save(PublicMenu publicMenu) {
		BaseResult result = new BaseResult();

		try {
			publicMenuServiceImpl.add(publicMenu);
			result.data = publicMenu.getGid();
		} catch (IllegalExistCodeException e) {
			result.code = 401;
		} catch (IllegalExceedRootLimitException e) {
			result.code = 402;
		} catch (IllegalExceedSubLimitException e) {
			result.code = 403;
		}

		return result;
	}

	@RequestMapping(value = "/delete.api")
	public BaseResult delete(PublicMenu publicMenu) {
		BaseResult result = new BaseResult();
		publicMenuServiceImpl.delete(publicMenu);
		return result;
	}

}
