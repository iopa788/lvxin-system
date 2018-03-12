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



import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.model.Manager;
import com.farsunset.lvxin.repository.ManagerRepository;
import com.farsunset.lvxin.service.ManagerService;

@Service
public class ManagerServiceImpl implements ManagerService {
	@Autowired
	private ManagerRepository managerRepository;
	@Value("${sys.manager.account}")
	private String account;
	@Value("${sys.manager.password}")
	private String password;
	@Value("${sys.manager.name}")
	private String name;
	
	@Override
	public void insertDefaultManager()   {
		int count = managerRepository.getManagerCount();
		if (count == 0) {
			Manager defManger = new Manager();
			defManger.setAccount(account);
			defManger.setName(name);
			defManger.setPassword(DigestUtils.md5Hex(password));
			managerRepository.save(defManger);
		}
	}

	@Override
	public Manager queryByAccount(String account) {
		return managerRepository.get(account);
	}
	
	@Override
	public void updatePassword(Manager manager) {
		// TODO Auto-generated method stub
		managerRepository.updatePassword(manager);
	}
}
