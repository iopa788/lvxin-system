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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.dto.ContactsResult;
import com.farsunset.lvxin.service.GroupService;
import com.farsunset.lvxin.service.OrganizationService;
import com.farsunset.lvxin.service.PublicAccountService;
import com.farsunset.lvxin.service.SQLiteDatabaseService;
import com.farsunset.lvxin.service.UserService;

@RestController
@RequestMapping("/cgi/contacts")
public class APIContactsController {

	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private GroupService groupServiceImpl;

	@Autowired
	private SQLiteDatabaseService sqLiteDatabaseService;

	@Autowired
	private PublicAccountService publicAccountService;

	@RequestMapping(value = "/sync.api")
	public ContactsResult sync(@TokenAccount String account) {

		ContactsResult result = new ContactsResult();

		result.groupList = groupServiceImpl.getRelevantList(account);

		result.pubAccountList = publicAccountService.queryListByUserAccount(account);

		return result;
	}

	@RequestMapping(value = "/get.api")
	public ContactsResult get() {
		ContactsResult result = new ContactsResult();
		result.userList = userService.getAll();
		result.orgList = organizationService.queryList();
		return result;
	}

	@RequestMapping(value = "/database.api")
	public void database(HttpServletResponse response) throws IOException {

		if (!sqLiteDatabaseService.isDbFileReady()) {
			sqLiteDatabaseService.createDatabase(false);
		}
		File dbFile = sqLiteDatabaseService.getDatabaseFile();
		response.setContentType(Files.probeContentType(Paths.get(dbFile.getAbsolutePath())));
		response.setContentLengthLong(dbFile.length());
		response.setHeader("Content-Disposition", "attachment;fileName=" + dbFile.getName());
		FileInputStream stream = FileUtils.openInputStream(dbFile);
		IOUtils.copy(stream, response.getOutputStream());
		IOUtils.closeQuietly(stream);
	}

}
