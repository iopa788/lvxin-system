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

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.farsunset.lvxin.cim.push.PubAccountMessagePusher;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.mvc.container.ContextHolder;
import com.farsunset.lvxin.service.PublicAccountService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.web.jstl.Page;

@Controller
@RequestMapping("/console/publicAccount")
public class PublicAccountController   {

	@Autowired
	private PublicAccountService publicAccountServiceImpl;

	@RequestMapping(value = "/list.action")
	public ModelAndView list(@ModelAttribute("publicAccount") PublicAccount publicAccount,
			@ModelAttribute("page") Page page) {

		publicAccountServiceImpl.queryPage(publicAccount, page);

		ModelAndView model = new ModelAndView();
		model.addObject("page", page);
		model.setViewName("publicAccount/manage");
		return model;
	}

	@RequestMapping(value = "/detailed.action")
	public @ResponseBody HashMap<String, Object> detailed(@RequestParam(value = "account") String account) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", 200);
		map.put("data", publicAccountServiceImpl.queryById(account));
		return map;
	}

	@RequestMapping(value = "/modifyBasic.action")
	public @ResponseBody HashMap<String, Object> modifyBasic(PublicAccount publicAccount) {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 404);

		PublicAccount target = publicAccountServiceImpl.queryById(publicAccount.getAccount());
		if (target != null) {
			target.setDescription(publicAccount.getDescription());
			target.setLink(publicAccount.getLink());
			target.setGreet(publicAccount.getGreet());
			target.setName(publicAccount.getName());
			publicAccountServiceImpl.update(target);
			datamap.put("code", 200);
		}

		return datamap;
	}

	@RequestMapping(value = "/modifyApi.action")
	public @ResponseBody HashMap<String, Object> modifyApi(PublicAccount publicAccount) {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 404);
		PublicAccount target = publicAccountServiceImpl.queryById(publicAccount.getAccount());
		if (target != null) {
			target.setApiUrl(publicAccount.getApiUrl());
			publicAccountServiceImpl.update(target);
			datamap.put("code", 200);
		}
		return datamap;
	}

	@RequestMapping(value = "/save.action")
	public @ResponseBody HashMap<String, Object> save(PublicAccount publicAccount) {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);
		try {
			publicAccountServiceImpl.save(publicAccount);
		} catch (IllegalArgumentException e) {
			datamap.put("code", 400);
		}

		return datamap;
	}

	@RequestMapping(value = "/setLogo.action")
	public void setLogo(MultipartFile file, String account, HttpServletResponse response) throws IOException {
		int code = 200;

		if (ImageIO.read(file.getInputStream()) == null) {
			code = 403;
		} else {
			publicAccountServiceImpl.updateLogo(account,file);
		}
		response.getWriter().print("<script>parent.onFileUploadCallbak(" + code + ")</script>");
	}

	@RequestMapping(value = "/push.action")
	public @ResponseBody HashMap<String, Object> push(Message message) {

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("code", 200);

		if (StringUtils.isEmpty(message.getReceiver())) {
			datamap.put("code", 500);
			datamap.put("message", "receiver不能空");
			return datamap;
		}

		message.setSender(Constants.SYSTEM);
		ContextHolder.getBean(PubAccountMessagePusher.class).push(message);

		return datamap;
	}

}
