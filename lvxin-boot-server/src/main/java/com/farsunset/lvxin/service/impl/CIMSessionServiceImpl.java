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
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.farsunset.cim.sdk.server.handler.CIMNioSocketAcceptor;
import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.redis.template.SessionRedisTemplate;
import com.farsunset.lvxin.repository.SessionRepository;
import com.farsunset.lvxin.service.CIMSessionService;

@Service
public class CIMSessionServiceImpl implements CIMSessionService {

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private SessionRedisTemplate sessionRedisTemplate;

	@Autowired
	private CIMNioSocketAcceptor cimNioSocketAcceptor;

	@Value("${server.host}")
	private String host;

	@Override
	public void save(CIMSession session) {
		sessionRepository.saveAndFlush(session);
		sessionRedisTemplate.save(session);
	}

	@Override
	public CIMSession get(String account) {

		CIMSession session = sessionRedisTemplate.get(account);
		if (session == null) {
			session = sessionRepository.findOne(account);
			sessionRedisTemplate.saveOrRemove(account, session);
		}

		if (session != null && (Objects.equals(session.getHost(), host))) {
			session.setIoSession(cimNioSocketAcceptor.getManagedSession(session.getNid()));
		}

		return session;
	}

	@Override
	public List<CIMSession> queryOnlineList() {
		List<CIMSession> list = sessionRepository.queryOnlineList();
		for (CIMSession session : list) {
			if ((Objects.equals(session.getHost(), host))) {
				session.setIoSession(cimNioSocketAcceptor.getManagedSession(session.getNid()));
			}
		}
		return list;
	}

	@Override
	public void remove(String uid) {
		sessionRepository.delete(uid);
		sessionRedisTemplate.delete(uid);
	}

	@Override
	public Page<CIMSession> queryPage(CIMSession session, Pageable page) {
		Specification<CIMSession> specification = new Specification<CIMSession>() {

			@Override
			public Predicate toPredicate(Root<CIMSession> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				if (StringUtils.isNotBlank(session.getAccount())) {
					predicatesList.add(builder.equal(root.get("account").as(String.class), session.getAccount()));
				}
				if (session.getNid() != null) {
					predicatesList.add(builder.equal(root.get("nid").as(Long.class), session.getNid()));
				}
				if (StringUtils.isNotBlank(session.getChannel())) {
					predicatesList.add(builder.equal(root.get("channel").as(String.class), session.getChannel()));
				}
				if (StringUtils.isNotBlank(session.getClientVersion())) {
					predicatesList
							.add(builder.equal(root.get("clientVersion").as(String.class), session.getClientVersion()));
				}

				if (StringUtils.isNotBlank(session.getSystemVersion())) {
					predicatesList
							.add(builder.equal(root.get("systemVersion").as(String.class), session.getSystemVersion()));
				}
				predicatesList.add(builder.equal(root.get("status").as(Integer.class), session.getStatus()));

				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				query.orderBy(builder.desc(root.get("bindTime").as(Long.class)));
				return query.getRestriction();
			}
		};
		return this.sessionRepository.findAll(specification, page);
	}

}
