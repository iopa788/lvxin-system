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
package com.farsunset.lvxin.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.farsunset.lvxin.exception.EmptyExcelFileException;
import com.farsunset.lvxin.exception.IllegalNullArgumentException;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.model.User;

public class TemplateTools {
	public static List<Organization> parseOrganizationList(MultipartFile excelFile) throws IOException {
		InputStream stream = excelFile.getInputStream();
		List<String> itemList = IOUtils.readLines(stream, Charset.forName("GB2312"));
		int rowCount = itemList.size();
		if (rowCount <= 1) {
			IOUtils.closeQuietly(stream);
			throw new EmptyExcelFileException();
		}

		List<Organization> list = new ArrayList<Organization>();

		for (int row = 1; row < rowCount; row++) {
			String[] array = StringUtils.splitPreserveAllTokens(itemList.get(row), ',');
			Organization model = new Organization();
			model.setCode(array[0]);
			model.setName(array[1]);
			model.setParentCode(StringUtils.isEmpty(array[2]) ? null : array[2]);

			if (StringUtils.isEmpty(model.getCode()) || StringUtils.isEmpty(model.getName())) {
				throw new IllegalNullArgumentException();
			}
			list.add(model);
		}

		IOUtils.closeQuietly(stream);
		return list;
	}

	public static List<User> parseUserList(MultipartFile excelFile) throws IOException {
		InputStream stream = excelFile.getInputStream();
		List<String> itemList = IOUtils.readLines(stream, Charset.forName("GB2312"));
		int rowCount = itemList.size();
		if (rowCount <= 1) {
			IOUtils.closeQuietly(stream);
			throw new EmptyExcelFileException();
		}

		List<User> list = new ArrayList<User>();
		String defPasswrod = DigestUtils.md5DigestAsHex(Constants.DEF_PASSWORD.getBytes());
		for (int row = 1; row < rowCount; row++) {
			String[] array = StringUtils.splitPreserveAllTokens(itemList.get(row), ',');
			User model = new User();
			model.setAccount(array[0]);
			model.setName(array[1]);
			model.setGender(StringUtils.isEmpty(array[2]) ? null : array[2]);
			model.setTelephone(StringUtils.isEmpty(array[3]) ? null : array[3]);
			model.setEmail(StringUtils.isEmpty(array[4]) ? null : array[4]);
			model.setOrgCode(StringUtils.isEmpty(array[5]) ? null : array[5]);

			if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getName())) {
				throw new IllegalNullArgumentException();
			}

			model.setPassword(defPasswrod);
			list.add(model);
		}

		IOUtils.closeQuietly(stream);
		return list;

	}

}
