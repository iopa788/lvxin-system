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
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.UnsupportedFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.farsunset.lvxin.exception.EmptyExcelFileException;
import com.farsunset.lvxin.exception.IllegalNullArgumentException;
import com.farsunset.lvxin.exception.IllegalUserAccountException;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.OrganizationService;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.TemplateTools;
import com.farsunset.lvxin.web.jstl.Page;

@Controller
@RequestMapping("/console/user")
public class UserController {

	@Autowired
	private UserService userServiceImpl;

	@Autowired
	private OrganizationService organizationServiceImpl;

	@RequestMapping(value = "/list.action")
	public ModelAndView list(@ModelAttribute("user") User user, @ModelAttribute("page") Page page) {

		ModelAndView model = new ModelAndView();
		userServiceImpl.queryList(user, page);
		model.addObject("page", page);
		model.setViewName("user/manage");
		return model;

	}

	@RequestMapping(value = "/add.action")
	public @ResponseBody HashMap<String, Object> add(User user) {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);

		if (StringUtils.isNotEmpty(user.getOrgCode())) {
			Organization org = organizationServiceImpl.queryByCode(user.getOrgCode());
			if (org == null) {
				datamap.put("code", 404);
				return datamap;
			}
		}

		user.setPassword(DigestUtils.md5Hex(Constants.DEF_PASSWORD));
		
		try {
			userServiceImpl.save(user);
		}catch(IllegalUserAccountException e) {
			datamap.put("code", 101);
		}
		return datamap;
	}

	@RequestMapping(value = "/update.action")
	public @ResponseBody HashMap<String, Object> update(User user) {

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);

		if (StringUtils.isNotEmpty(user.getOrgCode())) {
			Organization org = organizationServiceImpl.queryByCode(user.getOrgCode());
			if (org == null) {
				datamap.put("code", 404);
				return datamap;
			}
		}

		User target = userServiceImpl.get(user.getAccount());
		target.setName(user.getName());
		target.setEmail(user.getEmail());
		target.setTelephone(user.getTelephone());
		target.setGender(user.getGender());
		target.setOrgCode(user.getOrgCode());
		userServiceImpl.update(target);
		return datamap;
	}

	@RequestMapping(value = "/resetPassword.action")
	public @ResponseBody HashMap<String, Object> resetPassword(String account) {

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);
		User target = userServiceImpl.get(account);
		target.setPassword(DigestUtils.md5Hex(Constants.DEF_PASSWORD));
		userServiceImpl.updateQuietly(target);
		return datamap;
	}

	@RequestMapping(value = "/toogleState.action")
	public @ResponseBody HashMap<String, Object> toogleState(String account, String state) {

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);
		userServiceImpl.updateState(account, state);
		return datamap;
	}

	// @RequestMapping(value = "/delete.action")
	public @ResponseBody HashMap<String, Object> delete(String account) {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);
		userServiceImpl.delete(account);
		return datamap;
	}

	@RequestMapping(value = "/importExcel.action")
	public void importExcel(MultipartFile excelFile, HttpServletResponse response) throws IOException {

		int code = 200;
		try {

			List<User> list = TemplateTools.parseUserList(excelFile);
			userServiceImpl.saveBatch(list);

		} catch (IOException error) {
			code = 10011;
		} catch (IllegalNullArgumentException error) {
			code = 10012;
		} catch (EmptyExcelFileException error) {
			code = 10010;
		} catch (IllegalUserAccountException error) {
			code = 10013;
		} catch (UnsupportedFileFormatException error) {
			code = 10011;
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print("<script>parent.onFileUploadCallbak(" + code + ");</script>");
	}

}
