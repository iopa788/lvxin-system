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
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.farsunset.cim.sdk.server.handler.CIMNioSocketAcceptor;
import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.redis.template.SessionRedisTemplate;
import com.farsunset.lvxin.repository.SessionRepository;
import com.farsunset.lvxin.service.CIMSessionService;
import com.farsunset.lvxin.web.jstl.Page;
 
@Service
public class CIMSessionServiceImpl implements CIMSessionService {
	
	@Autowired
	SessionRepository sessionRepository;

	@Autowired
	SessionRedisTemplate  sessionRedisTemplate;

	@Autowired
	CIMNioSocketAcceptor cimNioSocketAcceptor;

	@Value("${cim.server.host}") 
	private String host;
	
	@Override
	public void save(CIMSession session) {
		sessionRepository.saveOrUpdate(session);
		sessionRedisTemplate.save(session);
	}

	@Override
	public CIMSession get(String account) {


		CIMSession session = sessionRedisTemplate.get(account);
		if (session == null) {
			session = sessionRepository.get(account);
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
	public Page queryPage(CIMSession session,Page page) {
		int count = this.sessionRepository.queryAmount(session);
		page.setCount(Integer.valueOf(count));
		if (page.getCount() == 0) {
			return page;
		}
		this.sessionRepository.queryByPage(session, page);
		return page;
	}

}
