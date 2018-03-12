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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.service.BottleService;

@RestController
@RequestMapping("/cgi/bottle")
public class APIBottleController {

	@Autowired
	private BottleService bottleService;

	@RequestMapping(value = "/release.api")
	public BaseResult release(Bottle bottle, @TokenAccount String account) {
		BaseResult result = new BaseResult();
		bottle.setSender(account);
		bottleService.save(bottle);
		result.data = bottle.getGid();

		return result;
	}

	@RequestMapping(value = "/received.api")
	public BaseResult received(@TokenAccount String account, String gid) {
		BaseResult result = new BaseResult();
		result.code = 200;
		Bottle target = bottleService.queryById(gid);
		if (target == null) {
			result.code = 404;
		} else if (target.getStatus() == Bottle.STATUS_RECEIVED) {
			result.code = 403;
		} else {
			target.setReceiver(account);
			target.setStatus(Bottle.STATUS_RECEIVED);
		}

		bottleService.update(target);
		return result;
	}

	@RequestMapping(value = "/detailed.api")
	public BaseResult detailed(String gid) {

		BaseResult result = new BaseResult();
		result.code = 404;
		Bottle bottle = bottleService.queryById(gid);
		if (bottle != null) {
			result.code = 200;
			result.data = bottle;
		}
		return result;
	}

	@RequestMapping(value = "/delete.api")
	public BaseResult delete(Bottle bottle) {

		BaseResult result = new BaseResult();
		bottleService.delete(bottle);
		return result;
	}

	@RequestMapping(value = "/get.api")
	public BaseResult get(@TokenAccount String account) {

		BaseResult result = new BaseResult();
		result.data = bottleService.getRandomOne(account);
		result.code = result.data != null ? 200 : 404;

		return result;
	}

}
