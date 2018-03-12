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
import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.service.impl.CIMSessionServiceImpl;

@RestController
@RequestMapping("/cgi/session")
public class APISessionController {

	@Autowired
	private CIMSessionServiceImpl cimSessionService;

	@RequestMapping(value = "/openApns.api")
	public BaseResult openApns(@TokenAccount String account) {
		BaseResult result = new BaseResult();

		CIMSession session = cimSessionService.get(account);
		if (session != null) {
			session.setApnsAble(CIMSession.APNS_ON);
			cimSessionService.save(session);
		}
		return result;
	}

	@RequestMapping(value = "/closeApns.api")
	public BaseResult closeApns(@TokenAccount String account) {

		BaseResult result = new BaseResult();

		CIMSession session = cimSessionService.get(account);
		if (session != null) {
			session.setApnsAble(CIMSession.APNS_OFF);
			cimSessionService.save(session);
		}
		return result;
	}
}
