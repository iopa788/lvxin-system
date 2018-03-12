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

import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.repository.OrganizationRepository;
import com.farsunset.lvxin.service.OrganizationService;
import com.farsunset.lvxin.service.SQLiteDatabaseService;
import com.farsunset.lvxin.web.jstl.Page;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;
	@Autowired
	private SQLiteDatabaseService sqLiteDatabaseService;
	@Override
	public void add(Organization model) {

		organizationRepository.save(model);
		sqLiteDatabaseService.add(model);
	}

	@Override
	public Organization queryByCode(String code) {
		return organizationRepository.get(code);
	}

	@Override
	public void delete(String code) {
		Organization target = new Organization();
		target.setCode(code);
		organizationRepository.delete(target);
		sqLiteDatabaseService.delete(target);
	}

	@Override
	public List<Organization> queryList() {
		// TODO Auto-generated method stub
		return organizationRepository.getAll();
	}

	@Override
	public void queryPage(Organization model, Page page) {
		int count = this.organizationRepository.queryAmount(model);
		page.setCount(Integer.valueOf(count));
		if (page.getCount() == 0) {
			return;
		}

		this.organizationRepository.queryPage(model, page);
	}

	@Override
	public void update(Organization model) {
		// TODO Auto-generated method stub
		organizationRepository.update(model);
		sqLiteDatabaseService.delete(model);
	}

	@Override
	public int saveBatch(List<Organization> list) {

		int count = organizationRepository.saveList(list);
		sqLiteDatabaseService.createOrgDatabase();
		return count;
	}

	@Override
	public List<Organization> queryChildList(String code) {
		return organizationRepository.queryChildList(code);
	}

}
