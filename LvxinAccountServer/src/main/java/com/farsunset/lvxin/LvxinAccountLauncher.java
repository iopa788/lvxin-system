package com.farsunset.lvxin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechUtility;

@SpringBootApplication
public class LvxinAccountLauncher {
	public static void main(String[] args) throws Exception {
		SpeechUtility.createUtility(SpeechConstant.APPID + "=55976c4e");
		SpringApplication.run(LvxinAccountLauncher.class, args);
	}
}
