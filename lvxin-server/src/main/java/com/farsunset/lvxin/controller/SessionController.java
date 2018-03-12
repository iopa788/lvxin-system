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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farsunset.cim.sdk.server.constant.CIMConstant;
import com.farsunset.cim.sdk.server.handler.CIMNioSocketAcceptor;
import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.mvc.container.ContextHolder;
import com.farsunset.lvxin.service.impl.CIMSessionServiceImpl;
import com.farsunset.lvxin.web.jstl.Page;

@Controller
@RequestMapping("/console/session")
public class SessionController {

	@Autowired
	private CIMSessionServiceImpl cimSessionService;

	@RequestMapping(value = "/list.action")
	public ModelAndView list(@ModelAttribute("cimsession") CIMSession cimsession, @ModelAttribute("page") Page page) {
		cimsession.setStatus(CIMSession.STATUS_ENABLED);
		ModelAndView model = new ModelAndView();
		cimSessionService.queryPage(cimsession, page);
		model.setViewName("session/manage");
		return model;
	}

	@RequestMapping(value = "/logcat.action")
	public @ResponseBody List<HashMap<String, Object>> logcat() {

		Map<Long, IoSession> map = ContextHolder.getBean(CIMNioSocketAcceptor.class).getManagedSessions();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (Long key : map.keySet()) {
			HashMap<String, Object> sesion = new HashMap<String, Object>();
			IoSession ioSession = map.get(key);
			sesion.put("nid", ioSession.getId());
			sesion.put("address", ioSession.getRemoteAddress());
			sesion.put("account", ioSession.getAttribute(CIMConstant.SESSION_KEY));
			list.add(sesion);
		}

		return list;
	}

}
