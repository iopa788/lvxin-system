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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.farsunset.cim.sdk.server.constant.CIMConstant;
import com.farsunset.cim.sdk.server.handler.CIMNioSocketAcceptor;
import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.service.CIMSessionService;
import com.farsunset.lvxin.util.Constants;

@Controller
@RequestMapping("/console/session")
public class SessionController {

	@Autowired
	private CIMSessionService cimSessionService;

	@Autowired
	CIMNioSocketAcceptor cimNioSocketAcceptor;

	@RequestMapping(value = "/list.action")
	public String list(CIMSession cimsession, @PageNumber int currentPage, Model model) {

		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<CIMSession> page = cimSessionService.queryPage(cimsession, pageable);
		model.addAttribute("page", page);
		model.addAttribute("cimsession", cimsession);
		return "console/session/manage";

	}

	@RequestMapping(value = "/logcat.action")
	public @ResponseBody List<HashMap<String, Object>> logcat() {

		Map<Long, IoSession> map = cimNioSocketAcceptor.getManagedSessions();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (Long key : map.keySet()) {
			HashMap<String, Object> sesion = new HashMap<String, Object>();
			IoSession ioSession = map.get(key);
			sesion.put("nid", ioSession.getId());
			sesion.put("address", ioSession.getRemoteAddress().toString());
			sesion.put("account", ioSession.getAttribute(CIMConstant.SESSION_KEY));
			list.add(sesion);
		}

		return list;
	}

}
