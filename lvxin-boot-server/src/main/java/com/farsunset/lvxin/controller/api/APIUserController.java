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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.annotation.AccessToken;
import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.dto.Page;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.AccessTokenService;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.service.impl.CIMSessionServiceImpl;

@RestController
@RequestMapping("/cgi/user")
public class APIUserController {

	@Autowired
	private UserService userServiceImpl;
	@Autowired
	AccessTokenService accessTokenService;
	@Autowired
	private CIMSessionServiceImpl cimSessionService;

	@RequestMapping(value = "/detailed.api")
	public BaseResult detailed(String account) {
		BaseResult result = new BaseResult();
		result.code = 200;
		User user = userServiceImpl.get(account);
		if (user == null) {
			result.code = 404;
		} else {
			result.data = user;
		}
		return result;
	}

	@RequestMapping(value = "/login.api")
	public BaseResult login(User user) {

		String password = user.getPassword();
		BaseResult result = new BaseResult();

		user = userServiceImpl.get(user.getAccount());

		if (user == null) {
			result.code = 404;
			return result;
		}

		if (user.isDisabled()) {
			result.code = 405;
			return result;
		}

		if (!password.equalsIgnoreCase(user.getPassword())) {
			result.code = 403;
			return result;
		}

		result.data = user;

		result.token = accessTokenService.generate(user.getAccount());
		return result;
	}

	@RequestMapping(value = "/logout.api")
	public BaseResult logout(@AccessToken String token) {
		accessTokenService.delete(token);
		return new BaseResult();
	}

	@RequestMapping(value = "/modifyPassword.api")
	public BaseResult modifyPassword(String oldPassword, String newPassword, @TokenAccount String account) {

		BaseResult result = new BaseResult();
		User target = userServiceImpl.get(account);
		if (target.getPassword().equals(oldPassword)) {
			target.setPassword(newPassword);
			userServiceImpl.updateQuietly(target);
		} else {
			result.code = 403;
		}

		return result;
	}

	@RequestMapping(value = "/nearby.api")
	public BaseResult nearby(String gender, @TokenAccount String account, @PageNumber int currentPage) {
		BaseResult result = new BaseResult();
		result.dataList = userServiceImpl.queryNearbyList(account, gender);
		Page page = new Page();
		page.setCount(result.dataList.size());
		page.setCountPage(1);
		page.setCurrentPage(1);
		page.setSize(result.dataList.size());
		result.page = page;
		return result;
	}

	@RequestMapping(value = "/getOnlineState.api")
	public BaseResult getOnlineState(String targetAccount) {

		BaseResult result = new BaseResult();
		CIMSession targetSession = cimSessionService.get(targetAccount);
		if (targetSession != null && targetSession.isConnected()) {
			result.code = 200;
		} else {
			result.code = 404;
		}
		return result;
	}

}
