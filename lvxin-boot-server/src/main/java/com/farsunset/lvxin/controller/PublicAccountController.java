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

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.cim.push.PubAccountMessagePusher;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.service.PublicAccountService;
import com.farsunset.lvxin.util.Constants;

@Controller
@RequestMapping("/console/publicAccount")
public class PublicAccountController {

	@Autowired
	private PublicAccountService publicAccountService;

	@Autowired
	PubAccountMessagePusher pubAccountMessagePusher;

	@RequestMapping(value = "/list.action")
	public String list(PublicAccount publicAccount, @PageNumber int currentPage, Model model) {

		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<PublicAccount> page = publicAccountService.queryPage(publicAccount, pageable);
		model.addAttribute("page", page);
		model.addAttribute("publicAccount", publicAccount);
		return "console/publicAccount/manage";

	}

	@RequestMapping(value = "/detailed.action")
	public @ResponseBody BaseResult detailed(@RequestParam(value = "account") String account) {

		BaseResult result = new BaseResult();
		result.data = publicAccountService.queryById(account);
		return result;
	}

	@RequestMapping(value = "/modifyBasic.action")
	public @ResponseBody BaseResult modifyBasic(PublicAccount publicAccount) {

		BaseResult result = new BaseResult();

		PublicAccount target = publicAccountService.queryById(publicAccount.getAccount());
		if (target == null) {
			result.code = 404;
			return result;
		}
		target.setDescription(publicAccount.getDescription());
		target.setLink(publicAccount.getLink());
		target.setGreet(publicAccount.getGreet());
		target.setName(publicAccount.getName());
		publicAccountService.update(target);
		return result;
	}

	@RequestMapping(value = "/modifyApi.action")
	public @ResponseBody BaseResult modifyApi(PublicAccount publicAccount) {
		BaseResult result = new BaseResult();
		PublicAccount target = publicAccountService.queryById(publicAccount.getAccount());
		if (target == null) {
			result.code = 404;
			return result;

		}

		target.setApiUrl(publicAccount.getApiUrl());
		publicAccountService.update(target);
		return result;
	}

	@RequestMapping(value = "/save.action")
	public @ResponseBody BaseResult save(PublicAccount publicAccount) {
		BaseResult result = new BaseResult();
		try {
			publicAccountService.save(publicAccount);
		} catch (IllegalArgumentException e) {
			result.code = 400;
		}

		return result;
	}

	@RequestMapping(value = "/setLogo.action")
	public void setLogo(MultipartFile file, String account, HttpServletResponse response) throws IOException {
		int code = 200;

		if (ImageIO.read(file.getInputStream()) == null) {
			code = 403;
		} else {
			publicAccountService.updateLogo(account, file);
		}
		response.getWriter().print("<script>parent.onFileUploadCallbak(" + code + ")</script>");
	}

	@RequestMapping(value = "/push.action")
	public @ResponseBody BaseResult push(Message message) {

		BaseResult result = new BaseResult();
		message.setSender(Constants.SYSTEM);
		pubAccountMessagePusher.push(message);

		return result;
	}

}
