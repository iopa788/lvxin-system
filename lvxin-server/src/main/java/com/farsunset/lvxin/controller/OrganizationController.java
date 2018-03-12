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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.farsunset.lvxin.exception.IllegalExistCodeException;
import com.farsunset.lvxin.exception.IllegalNullArgumentException;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.service.OrganizationService;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.util.TemplateTools;
import com.farsunset.lvxin.web.jstl.Page;

@Controller
@RequestMapping("/console/organization")
public class OrganizationController {

	@Autowired
	private OrganizationService organizationServiceImpl;

	@Autowired
	private UserService userServiceImpl;

	
	@RequestMapping(value = "/list.action")
	public ModelAndView list(@ModelAttribute("org") Organization org, @ModelAttribute("page") Page page) {

		organizationServiceImpl.queryPage(org, page);

		ModelAndView model = new ModelAndView();
		model.addObject("page", page);
		model.setViewName("organization/manage");
		return model;
	}

	@RequestMapping(value = "/importExcel.action")
	public void importExcel(MultipartFile excelFile, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		int code = 200;
		try {

			List<Organization> list = TemplateTools.parseOrganizationList(excelFile);

			organizationServiceImpl.saveBatch(list);
		} catch (IOException error) {
			code = 10011;
		} catch (IllegalNullArgumentException error) {
			code = 10012;
		} catch (EmptyExcelFileException error) {
			code = 10010;
		} catch (IllegalExistCodeException error) {
			code = 10013;
		}catch (UnsupportedFileFormatException error) {
			code = 10011;
		}

		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print("<script>parent.onFileUploadCallbak(" + code + ");</script>");
	}

	@RequestMapping(value = "/update.action")
	public @ResponseBody HashMap<String, Object> update(Organization org) {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);
		if (StringUtils.isNotEmpty(org.getParentCode())) {
			Organization parent = organizationServiceImpl.queryByCode(org.getParentCode());
			if (parent == null) {
				datamap.put("code", 404);
				return datamap;
			}
		}

		organizationServiceImpl.update(org);

		return datamap;
	}

	@RequestMapping(value = "/save.action")
	public @ResponseBody HashMap<String, Object> save(Organization org) {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);

		if (StringUtils.isNotEmpty(org.getParentCode())) {
			Organization parent = organizationServiceImpl.queryByCode(org.getParentCode());
			if (parent == null) {
				datamap.put("code", 404);
				return datamap;
			}
		}
		Organization target = organizationServiceImpl.queryByCode(org.getCode());
		if (target != null) {
			datamap.put("code", 101);
			return datamap;
		}
		organizationServiceImpl.add(org);
		return datamap;
	}

	@RequestMapping(value = "/delete.action")
	public @ResponseBody HashMap<String, Object> delete(String code) {
		HashMap<String, Object> datamap = new HashMap<String, Object>();

		if (!organizationServiceImpl.queryChildList(code).isEmpty()) {
			datamap.put("code", 403);
			return datamap;
		}
		
		int count = userServiceImpl.countOrganizationMember(code);
		if (count > 0) {
			datamap.put("code", 405);
			return datamap;
		}
		
		datamap.put("code", 200);
		organizationServiceImpl.delete(code);
		return datamap;
	}

}
