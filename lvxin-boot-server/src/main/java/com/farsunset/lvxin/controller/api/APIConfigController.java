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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.exception.IllegalExistCodeException;
import com.farsunset.lvxin.model.Config;
import com.farsunset.lvxin.service.ConfigService;

@RestController
@RequestMapping("/cgi/config")
public class APIConfigController {

	@Autowired
	private ConfigService configService;

	@RequestMapping(value = "/list.api")
	public BaseResult list(String domain) {
		BaseResult result = new BaseResult();

		result.dataList = configService.queryListByDomain(domain);
		return result;
	}

	@RequestMapping(value = "/save.api")
	public BaseResult save(Config config) {
		BaseResult result = new BaseResult();

		try {
			configService.save(config);
		} catch (IllegalExistCodeException e) {
			result.code = 401;
		}
		return result;
	}

	@RequestMapping(value = "/get.api")
	public BaseResult get(Config config) {
		BaseResult result = new BaseResult();

		result.data = configService.querySingle(config);
		return result;
	}

	@RequestMapping(value = "/delete.api")
	public BaseResult delete(String gid) {
		BaseResult result = new BaseResult();

		configService.delete(gid);
		return result;
	}

	@RequestMapping(value = "/getNewVersion.api")
	public BaseResult getNewVersion(String domain, int versionCode) {
		BaseResult result = new BaseResult();
		Map<String, String> map = configService.queryMapByDomain(domain);
		String newVersionCode = map.get("versionCode");
		if (newVersionCode == null || Integer.parseInt(newVersionCode) <= versionCode) {
			result.code = 404;
		} else {
			result.data = map;
		}

		return result;
	}

}
