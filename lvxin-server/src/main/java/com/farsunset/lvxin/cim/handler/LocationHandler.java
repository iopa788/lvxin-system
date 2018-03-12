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
package com.farsunset.lvxin.cim.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.farsunset.cim.sdk.server.constant.CIMConstant;
import com.farsunset.cim.sdk.server.handler.CIMRequestHandler;
import com.farsunset.cim.sdk.server.model.ReplyBody;
import com.farsunset.cim.sdk.server.model.SentBody;
import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.service.impl.CIMSessionServiceImpl;

/**
 * 记录客户端上传的位置信息，用于需要使用位置的相关服务
 */
public class LocationHandler implements CIMRequestHandler {
	@Autowired
	private CIMSessionServiceImpl cimSessionService;
	@Autowired
	private UserService userServiceImpl;

	@Override
	public ReplyBody process(CIMSession session, SentBody message) {

		ReplyBody reply = new ReplyBody();
		reply.setCode(CIMConstant.ReturnCode.CODE_200);

		String account = message.get("account");
		Double longitude = Double.parseDouble(message.get("longitude"));
		Double latitude = Double.parseDouble(message.get("latitude"));
		String location = message.get("location");
		session = cimSessionService.get(account);
		session.setLongitude(longitude);
		session.setLatitude(latitude);
		session.setLocation(location);

		cimSessionService.save(session);

		User target = userServiceImpl.get(account);
		target.setLongitude(longitude);
		target.setLatitude(latitude);
		target.setLocation(location);
		userServiceImpl.updateQuietly(target);

		return reply;
	}
}
