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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.dto.ContactsResult;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.service.GroupService;
import com.farsunset.lvxin.service.OrganizationService;
import com.farsunset.lvxin.service.PublicAccountService;
import com.farsunset.lvxin.service.SQLiteDatabaseService;
import com.farsunset.lvxin.service.UserService;

@RestController
@RequestMapping("/cgi/contacts")
public class APIContactsController {

	@Autowired
	private UserService userServiceImpl;

	@Autowired
	private OrganizationService organizationServiceImpl;

	@Autowired
	private GroupService groupServiceImpl;

	@Autowired
	private SQLiteDatabaseService sqLiteDatabaseService;
	
	@Autowired
	private PublicAccountService publicAccountServiceImpl;

	@RequestMapping(value = "/sync.api")
	public ContactsResult sync(@TokenAccount String account) {

		ContactsResult result = new ContactsResult();

		List<Group> groupList = groupServiceImpl.getRelevantList(account);

		result.groupList = groupList;

		List<PublicAccount> pubAccountList = publicAccountServiceImpl.queryListByUserAccount(account);

		result.pubAccountList = pubAccountList;

		return result;
	}

	@RequestMapping(value = "/get.api")
	public ContactsResult get() throws IOException {
		ContactsResult result = new ContactsResult();
		result.userList = userServiceImpl.getAll();
		result.orgList = organizationServiceImpl.queryList();
		return result;
	}

	@RequestMapping(value = "/database.api")
	public void database(HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!sqLiteDatabaseService.isDbFileReady()) {
			sqLiteDatabaseService.createDatabase(false);
		}

		response.sendRedirect(sqLiteDatabaseService.getDbFileUrl(request));
	}

}
