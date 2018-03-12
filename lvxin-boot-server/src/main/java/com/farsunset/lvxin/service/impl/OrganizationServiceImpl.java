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
import java.util.List;

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

import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.repository.BatchAddRepository;
import com.farsunset.lvxin.repository.OrganizationRepository;
import com.farsunset.lvxin.service.OrganizationService;
import com.farsunset.lvxin.service.SQLiteDatabaseService;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private BatchAddRepository batchAddRepository;
	@Autowired
	private OrganizationRepository organizationRepository;
	@Autowired
	private SQLiteDatabaseService sqliteDatabaseService;

	@Override
	public void add(Organization model) {

		organizationRepository.save(model);
		sqliteDatabaseService.add(model);
	}

	@Override
	public Organization queryByCode(String code) {
		return organizationRepository.findOne(code);
	}

	@Override
	public void delete(String code) {
		Organization target = new Organization();
		target.setCode(code);
		organizationRepository.delete(target);
		sqliteDatabaseService.delete(target);
	}

	@Override
	public List<Organization> queryList() {
		return organizationRepository.findAll();
	}

	@Override
	public Page<Organization> queryPage(Organization model, Pageable pageable) {
		Specification<Organization> specification = new Specification<Organization>() {

			@Override
			public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(model.getName())) {
					predicatesList.add(builder.like(root.get("name").as(String.class), "%" + model.getName() + "%"));
				}
				if (StringUtils.isNotEmpty(model.getCode())) {
					predicatesList.add(builder.equal(root.get("code").as(String.class), model.getCode()));
				}
				if (StringUtils.isNotEmpty(model.getParentCode())) {
					predicatesList.add(builder.equal(root.get("parentCode").as(String.class), model.getParentCode()));
				}

				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				query.orderBy(builder.asc(root.get("sort").as(Long.class)));
				return query.getRestriction();
			}
		};

		return this.organizationRepository.findAll(specification, pageable);
	}

	@Override
	public void update(Organization model) {
		// TODO Auto-generated method stub
		organizationRepository.saveAndFlush(model);
		sqliteDatabaseService.delete(model);
	}

	@Override
	public void saveAll(List<Organization> list) {

		batchAddRepository.saveOrganization(list);
		sqliteDatabaseService.createOrgDatabase();
	}

	@Override
	public List<Organization> queryChildList(String code) {
		return organizationRepository.queryChildList(code);
	}

}
