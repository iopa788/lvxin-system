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
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.Host;
import com.farsunset.lvxin.service.HostService;

/**
 * 服务器调度实现
 */

@RestController
@RequestMapping("/cgi/host")
public class APIHostController {

	@Autowired
	private HostService hostService;

	/**
	 * 客户登录时调用此接口，为客户端随机分配一台服务器
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/dispense.api")
	public BaseResult dispense() {

		List<Host> list = hostService.queryList();
		BaseResult result = new BaseResult();
		result.data = list.get(new Random().nextInt(list.size())).getIp();
		return result;
	}

}
