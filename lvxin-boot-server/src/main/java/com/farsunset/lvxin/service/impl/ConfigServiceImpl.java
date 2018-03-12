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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
	public Config queryById(String gid) {
		return configRepository.findOne(gid);
	}

	@Override
	public void save(Config config) {
		config.setGid(UUIDTools.getUUID());
		if (StringUtils.isBlank(config.getDomain()) || StringUtils.isBlank(config.getKey())
				|| StringUtils.isBlank(config.getValue())) {
			throw new IllegalNullArgumentException();
		}
		if (configRepository.querySingle(config.getDomain(), config.getKey()) != null) {
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
		if (configRepository.countExist(config.getGid(), config.getDomain(), config.getKey()) > 0) {
			throw new IllegalExistCodeException();
		}
		configRepository.saveAndFlush(config);
	}

	@Override
	public List<String> queryDomainList() {
		return configRepository.queryDomainList();
	}

	@Override
	public Config querySingle(Config config) {
		return configRepository.querySingle(config.getDomain(), config.getKey());
	}

	@Override
	public List<Config> queryList(Config config) {
		Specification<Config> specification = new Specification<Config>() {
			@Override
			public Predicate toPredicate(Root<Config> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				if (!StringUtils.isEmpty(config.getDomain())) {
					predicatesList.add(builder.equal(root.get("domain").as(String.class), config.getDomain()));
				}
				if (!StringUtils.isEmpty(config.getKey())) {
					predicatesList.add(builder.equal(root.get("key").as(String.class), config.getKey()));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				return query.getRestriction();
			}
		};
		return configRepository.findAll(specification);
	}

	@Override
	public Map<String, String> queryMapByDomain(String domain) {
		List<Config> dataList = queryListByDomain(domain);
		HashMap<String, String> map = new HashMap<String, String>(dataList.size());
		for (Config config : dataList) {
			map.put(config.getKey(), config.getValue());
		}
		return map;
	}

	@Override
	public Page<Config> queryPage(Config config, Pageable pageable) {
		Specification<Config> specification = new Specification<Config>() {
			@Override
			public Predicate toPredicate(Root<Config> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				if (!StringUtils.isEmpty(config.getDomain())) {
					predicatesList.add(builder.equal(root.get("domain").as(String.class), config.getDomain()));
				}
				if (!StringUtils.isEmpty(config.getKey())) {
					predicatesList.add(builder.equal(root.get("key").as(String.class), config.getKey()));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				return query.getRestriction();
			}
		};
		return configRepository.findAll(specification, pageable);
	}
}
