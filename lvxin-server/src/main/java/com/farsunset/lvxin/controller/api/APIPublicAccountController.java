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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.cim.push.PubAccountMessagePusher;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.mvc.container.ContextHolder;
import com.farsunset.lvxin.service.PublicAccountService;
import com.farsunset.lvxin.util.Constants;

@RestController
@RequestMapping("/cgi/publicAccount")
public class APIPublicAccountController {

	@Autowired
	private PublicAccountService publicAccountServiceImpl;

	@RequestMapping(value = "/detailed.api")
	public BaseResult detailed(String account) {

		BaseResult result = new BaseResult();
		result.data = publicAccountServiceImpl.queryById(account);

		return result;
	}

	@RequestMapping(value = "/list.api")
	public BaseResult list(@TokenAccount String account) {

		BaseResult result = new BaseResult();
		List<PublicAccount> list = publicAccountServiceImpl.queryListByUserAccount(account);
		result.dataList = list;
		return result;
	}

	@RequestMapping(value = "/look.api")
	public BaseResult look() {

		BaseResult result = new BaseResult();
		List<PublicAccount> list = publicAccountServiceImpl.queryAll();
		result.dataList = list;
		return result;
	}

	@RequestMapping(value = "/push.api")
	public BaseResult push(Message message) {

		BaseResult result = new BaseResult();

		if (StringUtils.isEmpty(message.getReceiver())) {
			result.code = 500;
			result.message = "receiver不能空";
			return result;
		}

		message.setSender(Constants.SYSTEM);
		ContextHolder.getBean(PubAccountMessagePusher.class).push(message);

		return result;
	}

}
