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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.message.bean.PubMenuEvent;
import com.farsunset.lvxin.model.Subscriber;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.service.SubscriberService;

@RestController
@RequestMapping("/cgi/subscriber")
public class APISubscriberController {

	@Autowired
	private SubscriberService subscriberServiceImpl;

	@RequestMapping(value = "/list.api")
	public BaseResult list(String publicAccount) {

		BaseResult result = new BaseResult();

		List<User> subscriberList = subscriberServiceImpl.getSubscriberList(publicAccount);

		result.dataList = subscriberList;

		return result;
	}

	@RequestMapping(value = "/subscribe.api")
	public BaseResult subscribe(Subscriber subscriber,@TokenAccount String account) {

		BaseResult result = new BaseResult();
		subscriber.setAccount(account);
		subscriberServiceImpl.add(subscriber);
		subscriberServiceImpl.notifySubscribeEvent(subscriber, PubMenuEvent.EVENT_ACTION_SUBSCRIBE);
		return result;
	}

	@RequestMapping(value = "/unsubscribe.api")
	public BaseResult unsubscribe(Subscriber subscriber,@TokenAccount String account) {

		BaseResult result = new BaseResult();
		subscriber.setAccount(account);
		subscriberServiceImpl.delete(subscriber);
		subscriberServiceImpl.notifySubscribeEvent(subscriber, PubMenuEvent.EVENT_ACTION_UNSUBSCRIBE);
		return result;
	}

}
