package com.farsunset.lvxin.controller;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.model.PubMenuEvent;
import com.farsunset.lvxin.model.XFResult;
import com.google.gson.Gson;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.TextUnderstander;
import com.iflytek.cloud.speech.TextUnderstanderListener;
import com.iflytek.cloud.speech.UnderstanderResult;

@RestController
public class LXAccountController {

	@RequestMapping(value = "/handle.do")
	public String handle(@RequestBody PubMenuEvent event) throws IOException {
		if (event.eventType.equals(PubMenuEvent.EVENT_TYPE_SUBSCRIBE)) {
			return doOnSubscribeEvent(event);
		}

		if (event.eventType.equals(PubMenuEvent.EVENT_TYPE_UNSUBSCRIBE)) {
			return doOnUnSubscribeEvent(event);
		}

		if (event.eventType.equals(PubMenuEvent.EVENT_TYPE_TEXT)) {
			return doOnRecivedTextEvent(event);
		}

		if (event.eventType.equals(PubMenuEvent.EVENT_TYPE_MENU)) {
			return doOnMenuEvent(event);
		}

		return "{\"code\":403,\"message\":\"未知的事件:" + event.eventType + "\"}";
	}

	private String doOnRecivedTextEvent(PubMenuEvent event) throws IOException {
		final XFResult data = new XFResult();
		final TextUnderstander mTextUnderstander = TextUnderstander.createTextUnderstander();
		// 初始化监听器
		TextUnderstanderListener searchListener = new TextUnderstanderListener() {
			// 语义结果回调
			public void onResult(UnderstanderResult result) {

				XFResult temp = new Gson().fromJson(result.getResultString(), XFResult.class);
				data.answer = temp.answer;
				data.operation = temp.operation;
				data.service = temp.service;
				data.rc = temp.rc;
				data.returned = true;
				mTextUnderstander.cancel();
				mTextUnderstander.destroy();
				System.out.println("result:" + temp.toJSONString());
			}

			// 语义错误回调
			public void onError(SpeechError error) {
				data.returned = true;
				mTextUnderstander.cancel();
				mTextUnderstander.destroy();
				error.printStackTrace();
			}
		};
		// 开始语义理解
		mTextUnderstander.understandText(event.text, searchListener);

		while (!data.returned) {
			System.out.println("waiting.....");
		}

		if (data.getResult() != null) {

			return data.toJSONString();
		} else {
			return IOUtils.toString(LXAccountController.class.getResourceAsStream("/resopnse/text.txt"), "UTF-8");
		}
	}

	private String doOnMenuEvent(PubMenuEvent event) throws IOException {
		String responseJson;
		if (event.menuCode.equals("KEY_SUB_NEWS")) {
			responseJson = IOUtils.toString(LXAccountController.class.getResourceAsStream("/resopnse/news.txt"),
					"UTF-8");
			return responseJson;
		}
		if (event.menuCode.equals("KEY_SUB_WENZHANG")) {
			responseJson = IOUtils.toString(LXAccountController.class.getResourceAsStream("/resopnse/atricle.txt"),
					"UTF-8");
			return responseJson;
		}

		return null;
	}

	private String doOnSubscribeEvent(PubMenuEvent event) {
		// TODO 处理取消订阅的逻辑

		return "{\"code\":200}";
	}

	private String doOnUnSubscribeEvent(PubMenuEvent event) {
		// TODO 处理订阅的逻辑
		return "{\"code\":200}";
	}
}
