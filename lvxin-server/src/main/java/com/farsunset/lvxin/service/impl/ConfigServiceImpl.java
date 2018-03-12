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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.exception.IllegalExistCodeException;
import com.farsunset.lvxin.exception.IllegalNullArgumentException;
import com.farsunset.lvxin.model.Config;
import com.farsunset.lvxin.repository.ConfigRepository;
import com.farsunset.lvxin.service.ConfigService;
import com.farsunset.lvxin.util.UUIDTools;

@Service
public class ConfigServiceImpl implements ConfigService {
	@Autowired
	private ConfigRepository configRepository;

	@Override
	public Config queryById(String sequenceId) {
		return configRepository.get(sequenceId);
	}

	@Override
	public void save(Config config) {
		config.setGid(UUIDTools.getUUID());
		if (StringUtils.isBlank(config.getDomain()) || StringUtils.isBlank(config.getKey())
				|| StringUtils.isBlank(config.getValue())) {
			throw new IllegalNullArgumentException();
		}
		if (configRepository.querySingle(config) != null) {
			throw new IllegalExistCodeException();
		}
		configRepository.save(config);
	}

	@Override
	public List<Config> queryListByDomain(String domain) {
		return configRepository.queryListByDomain(domain);
	}

	@Override
	public void delete(String sequenceId) {
		Config config = new Config();
		config.setGid(sequenceId);
		configRepository.delete(config);
	}

	@Override
	public void update(Config config) {
		if (configRepository.hasExistent(config)) {
			throw new IllegalExistCodeException();
		}
		configRepository.update(config);
	}

	@Override
	public List<String> queryDomainList() {
		return configRepository.queryDomainList();
	}

	@Override
	public Config querySingle(Config config) {
		// TODO Auto-generated method stub
		return configRepository.querySingle(config);
	}

	@Override
	public List<Config> queryList(Config config) {
		// TODO Auto-generated method stub
		return configRepository.queryList(config);
	}

	@Override
	public Map<String, String> queryMapByDomain(String domain) {
		List<Config> dataList = queryListByDomain(domain);
		HashMap<String, String> map = new HashMap<String, String>();
		for (Config config : dataList) {
			map.put(config.getKey(), config.getValue());
		}
		return map;
	}
}
