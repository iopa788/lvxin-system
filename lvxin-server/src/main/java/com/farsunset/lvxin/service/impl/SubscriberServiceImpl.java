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
package com.farsunset.lvxin.service.impl;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.farsunset.lvxin.message.bean.PubMenuEvent;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.model.Subscriber;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.repository.SubscriberRepository;
import com.farsunset.lvxin.service.PublicAccountService;
import com.farsunset.lvxin.service.SubscriberService;
import com.farsunset.lvxin.util.UUIDTools;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class SubscriberServiceImpl implements SubscriberService {
	protected final Logger logger = Logger.getLogger(SubscriberServiceImpl.class);

	@Autowired
	private SubscriberRepository subscriberRepository;
	@Autowired
	private PublicAccountService publicAccountService;

	@Override
	public void add(Subscriber subscriber) {

		if (subscriberRepository.hasSubscriberRelation(subscriber)) {
			return;
		}
		subscriber.setTimestamp(String.valueOf(System.currentTimeMillis()));
		subscriber.setGid(UUIDTools.getUUID());
		subscriberRepository.save(subscriber);
	}

	@Override
	public void delete(Subscriber subscriber) {

		subscriberRepository.delete(subscriber);
	}

	@Override
	public List<User> getSubscriberList(String publicAccount) {

		return subscriberRepository.getSubscriberList(publicAccount);
	}

	@Override
	public List<String> getSubscriberAccounts(String account) {
		return subscriberRepository.getSubscriberAccounts(account);
	}

	@Override
	@Async
	public void notifySubscribeEvent(Subscriber subscriber, String eventType) {

		PublicAccount pubAccount = publicAccountService.queryById(subscriber.getPublicAccount());

		if (pubAccount == null || StringUtils.isEmpty(pubAccount.getApiUrl())) {
			return;
		}

		PubMenuEvent event = new PubMenuEvent();
		event.eventType = eventType;
		event.account = subscriber.getAccount();

		logger.info("eventType:" + eventType + " account:" + subscriber.getAccount() + " pubAccount:" + pubAccount);

		OkHttpClient httpclient = new OkHttpClient.Builder().build();
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(event));
		Request request = new Request.Builder().url(pubAccount.getApiUrl()).post(requestBody).build();
		Response response = null;

		try {

			response = httpclient.newCall(request).execute();
			String data = response.body().string();

			logger.info("result:" + data);

		} catch (Exception e) {
			logger.info(e);
		} finally {
			IOUtils.closeQuietly(response);
		}

	}

}
