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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.model.Host;
import com.farsunset.lvxin.repository.HostRepository;
import com.farsunset.lvxin.service.HostService;

/**
 * 服务器集群时，每台服务器的IP配置管理
 */
@Service
public class HostServiceImpl implements HostService {

	@Autowired
	private HostRepository hostRepository;

	@Override
	public void delete(String ip) {
		Host host = new Host();
		host.setIp(ip);
		hostRepository.delete(host);
	}

	@Override
	public List<Host> queryList() {
		return hostRepository.getAll();
	}

	@Override
	public void save(Host host) {
		hostRepository.save(host);
	}

}
