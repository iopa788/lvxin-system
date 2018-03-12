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
package com.farsunset.lvxin.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.farsunset.cim.sdk.server.model.Message;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MessageDispatcher {
	private static final Logger logger = Logger.getLogger(MessageDispatcher.class);
	private static ExecutorService executor = Executors.newCachedThreadPool();
	public static void execute(String dispathUrl,final Message msg, final String ip) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				String apiUrl = String.format(dispathUrl, ip);
				try {
					String response = httpPost(apiUrl, msg);
					logger.info("消息转发目标服务器{"+ip+"},结果:"+response);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("消息转发目标服务器"+apiUrl,e);
				}
			}
		});
	}

	private static String httpPost(String url, Message msg) throws Exception {

		OkHttpClient httpclient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
		FormBody.Builder build = new FormBody.Builder();

		if(msg.getMid()!=null) {
			build.add("mid", msg.getMid());	
		}
		if(msg.getAction()!=null) {
			build.add("action", msg.getAction());	
		}
		if(msg.getTitle()!=null) {
			build.add("title", msg.getTitle());	
		}
		if(msg.getContent()!=null) {
			build.add("content", msg.getContent());	
		}
		if(msg.getSender()!=null) {
			build.add("sender", msg.getSender());	
		}
		if(msg.getReceiver()!=null) {
			build.add("receiver", msg.getReceiver());	
		}
		if(msg.getFormat()!=null) {
			build.add("format", msg.getFormat());	
		}
		if(msg.getExtra()!=null) {
			build.add("extra", msg.getExtra());	
		}
		build.add("timestamp", String.valueOf(msg.getTimestamp()));
		Request request = new Request.Builder().url(url).post(build.build()).build();

		Response response = httpclient.newCall(request).execute();
		String data = response.body().string();

		IOUtils.closeQuietly(response);

		return data;
	}

}
