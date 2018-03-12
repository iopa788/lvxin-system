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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.farsunset.lvxin.model.Manager;
import com.farsunset.lvxin.service.ManagerService;
@RestController
@RequestMapping("/system")
public class SystemController {

	@Autowired
	private ManagerService managerServiceImpl;

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public   int login(String password, String account, HttpServletRequest request) throws Exception {

		Manager target = managerServiceImpl.queryByAccount(account);
		if (target != null && DigestUtils.md5Hex(password).equals(target.getPassword())) {

			request.getSession().setAttribute("manager", target);
			return 200;
		} else {
			return 403;
		}

	}

	@RequestMapping(value = "/manager/modifyPassword.action", method = RequestMethod.POST)
	public   int modifyPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		Manager manager = (Manager) request.getSession().getAttribute("manager");

		if (manager == null) {
			return 404;
		}
		if (!manager.getPassword().equals(DigestUtils.md5Hex(oldPassword))) {
			return 403;
		}
		manager.setPassword(DigestUtils.md5Hex(newPassword));
		managerServiceImpl.updatePassword(manager);
		return 200;
	}

}
