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

import com.farsunset.lvxin.model.MomentRule;
import com.farsunset.lvxin.repository.MomentRuleRepository;
import com.farsunset.lvxin.service.MomentRuleService;
import com.farsunset.lvxin.util.UUIDTools;

@Service
public class MomentRuleServiceImpl implements MomentRuleService {
	@Autowired
	private MomentRuleRepository momentRuleRepository;

	@Override
	public void delete(MomentRule model) {
		momentRuleRepository.delete(model);
	}

	@Override
	public List<String> queryMeLimitingList(String account) {
		return momentRuleRepository.queryMeLimitingList(account);
	}

	@Override
	public List<String> queryLimitingMeList(String account) {
		return momentRuleRepository.queryLimitingMeList(account);
	}

	@Override
	public void save(MomentRule model) {
		model.setGid(UUIDTools.getUUID());
		momentRuleRepository.save(model);
	}

	@Override
	public List<MomentRule> queryList(String account) {
		return momentRuleRepository.queryList(account);
	}

	@Override
	public boolean isBeLimiting(String account, String otherAccount) {
		return momentRuleRepository.isBeLimiting(account, otherAccount);
	}

	@Override
	public List<String> queryFilteredList(String account) {
		return momentRuleRepository.queryFilteredList(account);
	}

}
