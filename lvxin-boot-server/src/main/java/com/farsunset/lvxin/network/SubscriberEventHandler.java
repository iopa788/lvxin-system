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
package com.farsunset.lvxin.network;

import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.farsunset.lvxin.message.bean.PubMenuEvent;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.model.Subscriber;
import com.farsunset.lvxin.service.PublicAccountService;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class SubscriberEventHandler {
	private static final Logger logger = Logger.getLogger(SubscriberEventHandler.class.getName());

	@Autowired
	private PublicAccountService publicAccountService;

	/**
	 * 当有用户关注或者取消关注时，调用公众号服务端接口，因为是异步放法，需要在外部调用，所以分离开来
	 * 
	 * @param subscriber
	 *            关注着信息
	 * @param eventType
	 *            关注 或者 取消关注
	 */
	@Async
	public void handle(Subscriber subscriber, String eventType) {

		PublicAccount pubAccount = publicAccountService.queryById(subscriber.getPublicAccount());

		if (pubAccount == null || StringUtils.isEmpty(pubAccount.getApiUrl())) {
			return;
		}

		PubMenuEvent event = new PubMenuEvent();
		event.eventType = eventType;
		event.account = subscriber.getAccount();

		logger.info("eventType:" + eventType + " account:" + subscriber.getAccount() + " pubAccount:" + pubAccount);

		OkHttpClient httpclient = new OkHttpClient.Builder().build();
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(event));
		Request request = new Request.Builder().url(pubAccount.getApiUrl()).post(requestBody).build();
		Response response = null;

		try {

			response = httpclient.newCall(request).execute();
			String data = response.body().string();

			logger.info("result:" + data);

		} catch (Exception e) {
			logger.severe(e.getMessage());
		} finally {
			IOUtils.closeQuietly(response);
		}

	}

}
