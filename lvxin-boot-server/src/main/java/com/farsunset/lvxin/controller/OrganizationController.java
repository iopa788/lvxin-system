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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.exception.EmptyExcelFileException;
import com.farsunset.lvxin.exception.IllegalNullArgumentException;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.service.OrganizationService;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.TemplateTools;

@Controller
@RequestMapping("/console/organization")
public class OrganizationController {

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/list.action")
	public String list(Organization org, @PageNumber int currentPage, Model model) {

		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<Organization> page = organizationService.queryPage(org, pageable);
		model.addAttribute("page", page);
		model.addAttribute("org", org);
		return "console/organization/manage";
	}

	@RequestMapping(value = "/importExcel.action")
	public void importExcel(MultipartFile excelFile, HttpServletResponse response) throws IOException {

		int code = 200;
		try {

			List<Organization> list = TemplateTools.parseOrganizationList(excelFile);
			organizationService.saveAll(list);
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

	@RequestMapping(value = "/update.action")
	public @ResponseBody BaseResult update(Organization org) {
		BaseResult result = new BaseResult();
		if (StringUtils.isNotEmpty(org.getParentCode())) {
			Organization parent = organizationService.queryByCode(org.getParentCode());
			if (parent == null) {
				result.code = 404;
				return result;
			}
		}

		organizationService.update(org);

		return result;
	}

	@RequestMapping(value = "/save.action")
	public @ResponseBody BaseResult save(Organization org) {
		BaseResult result = new BaseResult();

		if (StringUtils.isNotEmpty(org.getParentCode())) {
			Organization parent = organizationService.queryByCode(org.getParentCode());
			if (parent == null) {
				result.code = 404;
				return result;
			}
		}
		Organization target = organizationService.queryByCode(org.getCode());
		if (target != null) {
			result.code = 101;
			return result;
		}
		organizationService.add(org);
		return result;
	}

	@RequestMapping(value = "/delete.action")
	public @ResponseBody BaseResult delete(String code) {
		BaseResult result = new BaseResult();

		if (!organizationService.queryChildList(code).isEmpty()) {
			result.code = 403;
			return result;
		}

		int count = userService.countOrganizationMember(code);
		if (count > 0) {
			result.code = 405;
			return result;
		}

		organizationService.delete(code);
		return result;
	}

}
