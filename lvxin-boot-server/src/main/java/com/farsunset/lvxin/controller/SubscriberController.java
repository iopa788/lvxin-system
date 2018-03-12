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
import com.farsunset.lvxin.service.SubscriberService;

@Controller
@RequestMapping("/console/subscriber")
public class SubscriberController {

	@Autowired
	private SubscriberService subscriberService;

	@RequestMapping(value = "/list.action")
	public @ResponseBody BaseResult list(String account) {

		BaseResult result = new BaseResult();
		result.dataList = subscriberService.getSubscriberList(account);
		return result;
	}

}
