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
package com.farsunset.lvxin.repository;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.farsunset.cim.sdk.server.model.CIMSession;
import com.farsunset.lvxin.web.jstl.Page;


@Repository
public class SessionRepository extends HibernateBaseDao<CIMSession> {

	public void queryByPage(CIMSession cimsession, Page page) {
		DetachedCriteria criteria = mapingParam(cimsession);
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));
	}

	public int queryAmount(CIMSession cimSession) {
		DetachedCriteria criteria = mapingParam(cimSession);
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	private DetachedCriteria mapingParam(CIMSession session) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CIMSession.class);

		if (StringUtils.isNotBlank(session.getAccount())) {
			criteria.add(Restrictions.eq("account", session.getAccount().trim()));
		}

		if (session.getNid() != null) {
			criteria.add(Restrictions.eq("nid", session.getNid()));
		}

		if (StringUtils.isNotBlank(session.getChannel())) {
			criteria.add(Restrictions.eq("channel", session.getChannel().trim()));
		}
		if (StringUtils.isNotBlank(session.getClientVersion())) {
			criteria.add(Restrictions.eq("clientVersion", session.getClientVersion().trim()));
		}

		if (StringUtils.isNotBlank(session.getSystemVersion())) {
			criteria.add(Restrictions.eq("systemVersion", session.getSystemVersion().trim()));
		}
		criteria.add(Restrictions.eq("status", session.getStatus()));
		criteria.addOrder(Order.desc("bindTime"));
		return criteria;
	}
 

	public void delete(String account) {
		Session session = currentSession();
		String sql = "delete   CIMSession   where account = :account";
		Query<?> query = session.createQuery(sql);
		query.setParameter("account", account);
		query.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	public List<CIMSession> queryOnlineList() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CIMSession.class);
		criteria.add(Restrictions.eq("status", CIMSession.STATUS_ENABLED));
		criteria.addOrder(Order.desc("bindTime"));
		return (List<CIMSession>) getHibernateTemplate().findByCriteria(criteria);
	}

}
