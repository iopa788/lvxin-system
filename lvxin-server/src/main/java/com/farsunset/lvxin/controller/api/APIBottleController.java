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

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.service.BottleService;
import com.farsunset.lvxin.web.jstl.Page;

@RestController
@RequestMapping("/cgi/bottle")
public class APIBottleController {

	@Autowired
	private BottleService bottleServiceImpl;

	@RequestMapping(value = "/release.api")
	public BaseResult release(Bottle bottle,@TokenAccount String account) throws InterruptedException {
		BaseResult result = new BaseResult();
		bottle.setSender(account);
		bottleServiceImpl.save(bottle);
		result.data = bottle.getGid();

		return result;
	}

	@RequestMapping(value = "/discard.api")
	public BaseResult discard(String gid) {
		BaseResult result = new BaseResult();

		bottleServiceImpl.updateReset(gid);

		return result;
	}

	@RequestMapping(value = "/get.api")
	public synchronized BaseResult get(@TokenAccount String account) {

		BaseResult result = new BaseResult();
		result.code = 404;
		int random = new Random().nextInt(100);
		if (random <= 33)// 每次有 1/3的概率可以打捞
		{

			Bottle target = bottleServiceImpl.queryRandom(account);
			if (target != null) {
				result.code = 200;
				target.setReceiver(account);
				target.setStatus(Bottle.STATUS_RECEIVED);
				bottleServiceImpl.update(target);
				result.data = target;
			}
		}
		return result;
	}

	@RequestMapping(value = "/received.api")
	public BaseResult received(@TokenAccount String account, String gid) {
		BaseResult result = new BaseResult();
		result.code = 200;
		Bottle target = bottleServiceImpl.queryById(gid);
		if (target == null || target.getStatus() == Bottle.STATUS_RECEIVED) {
			result.code = 403;
		} else {
			target.setReceiver(account);
			target.setStatus(Bottle.STATUS_RECEIVED);
		}

		bottleServiceImpl.update(target);
		return result;
	}

	@RequestMapping(value = "/detailed.api")
	public BaseResult detailed(String gid) {

		BaseResult result = new BaseResult();
		result.code = 404;
		Bottle bottle = bottleServiceImpl.queryById(gid);
		if (bottle != null) {
			result.code = 200;
			result.data = bottle;
		}
		return result;
	}

	@RequestMapping(value = "/delete.api")
	public BaseResult delete(Bottle bottle) {

		BaseResult result = new BaseResult();
		bottleServiceImpl.delete(bottle);
		return result;
	}

	@RequestMapping(value = "/list.api")
	public BaseResult list(@TokenAccount String account, Page page) {
		BaseResult result = new BaseResult();

		List<Bottle> list = bottleServiceImpl.queryNewList(account, page);
		result.dataList = list;

		return result;
	}

}
