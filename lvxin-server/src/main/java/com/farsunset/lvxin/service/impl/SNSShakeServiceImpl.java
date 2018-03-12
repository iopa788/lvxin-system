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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.model.SNSShake;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.repository.SNSShakeRepository;
import com.farsunset.lvxin.repository.UserRepository;
import com.farsunset.lvxin.service.SNSShakeService;

@Service
public class SNSShakeServiceImpl implements SNSShakeService {
	@Autowired
	private SNSShakeRepository snsShakeRepository;
	@Autowired
	private UserRepository userRepository;
	@Value("${sys.shake.effective}")
	private int effectiveTime;
	@Override
	public void deleteObsoleted() {
		snsShakeRepository.deleteObsoleted();
	}

	@Override
	public User queryRealtimeUser(String account) {
		int time = effectiveTime;
		long timestamp = System.currentTimeMillis() - time; 
		SNSShake target = snsShakeRepository.queryNewShake(account, timestamp);
		return target == null ? null : userRepository.get(target.getAccount());
	}

	@Override
	public void add(SNSShake model) {
		snsShakeRepository.save(model);
	}
}
