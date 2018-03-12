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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.exception.EmptyExcelFileException;
import com.farsunset.lvxin.exception.IllegalNullArgumentException;
import com.farsunset.lvxin.exception.IllegalUserAccountException;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.OrganizationService;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.TemplateTools;

@Controller
@RequestMapping("/console/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationService organizationService;

	@RequestMapping(value = "/list.action")
	public String list(User user, @PageNumber int currentPage, Model model) {

		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<User> page = userService.queryPage(user, pageable);
		model.addAttribute("page", page);
		model.addAttribute("user", user);
		return "console/user/manage";

	}

	@RequestMapping(value = "/add.action")
	public @ResponseBody BaseResult add(User user) {
		BaseResult result = new BaseResult();

		if (StringUtils.isNotEmpty(user.getOrgCode())) {
			Organization org = organizationService.queryByCode(user.getOrgCode());
			if (org == null) {
				result.code = 404;
				return result;
			}
		}

		user.setPassword(DigestUtils.md5DigestAsHex(Constants.DEF_PASSWORD.getBytes()));

		try {
			userService.save(user);
		} catch (IllegalUserAccountException e) {
			result.code = 101;
		}
		return result;
	}

	@RequestMapping(value = "/update.action")
	public @ResponseBody BaseResult update(User user) {

		BaseResult result = new BaseResult();

		if (StringUtils.isNotEmpty(user.getOrgCode())) {
			Organization org = organizationService.queryByCode(user.getOrgCode());
			if (org == null) {
				result.code = 404;
				return result;
			}
		}

		User target = userService.get(user.getAccount());
		target.setName(user.getName());
		target.setEmail(user.getEmail());
		target.setTelephone(user.getTelephone());
		target.setGender(user.getGender());
		target.setOrgCode(user.getOrgCode());
		userService.update(target);
		return result;
	}

	@RequestMapping(value = "/resetPassword.action")
	public @ResponseBody BaseResult resetPassword(String account) {

		BaseResult result = new BaseResult();

		User target = userService.get(account);
		target.setPassword(DigestUtils.md5DigestAsHex(Constants.DEF_PASSWORD.getBytes()));
		userService.updateQuietly(target);
		return result;
	}

	@RequestMapping(value = "/toogleState.action")
	public @ResponseBody BaseResult toogleState(String account, String state) {

		userService.updateState(account, state);
		return new BaseResult();
	}

	// @RequestMapping(value = "/delete.action")
	public @ResponseBody BaseResult delete(String account) {
		userService.delete(account);
		return new BaseResult();
	}

	@RequestMapping(value = "/importExcel.action")
	public void importExcel(MultipartFile excelFile, HttpServletResponse response) throws IOException {

		int code = 200;
		try {

			List<User> list = TemplateTools.parseUserList(excelFile);
			userService.saveAll(list);

		} catch (IOException | IndexOutOfBoundsException error) {
			code = 10011;
		} catch (IllegalNullArgumentException error) {
			code = 10012;
		} catch (EmptyExcelFileException error) {
			code = 10010;
		} catch (DataAccessException error) {
			code = 10013;
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print("<script>parent.onFileUploadCallbak(" + code + ");</script>");
	}

}
