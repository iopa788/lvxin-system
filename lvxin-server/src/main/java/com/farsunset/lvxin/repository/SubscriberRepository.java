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

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import com.farsunset.lvxin.model.Subscriber;
import com.farsunset.lvxin.model.User;

@Repository
public class SubscriberRepository extends HibernateBaseDao<Subscriber> {

	public void delete(Subscriber subscriber) {

		Session session = currentSession();
		String sql = "delete Subscriber where account= :account and publicAccount= :publicAccount ";
		Query<?> query = session.createQuery(sql);
		query.setParameter("account", subscriber.getAccount());
		query.setParameter("publicAccount", subscriber.getPublicAccount());
		query.executeUpdate();

	}

	public List<User> getSubscriberList(String publicAccount) {
		String sql = "select u  from User as u,Subscriber as s where u.account = s.account and s.publicAccount = :account order by s.timestamp desc";
		Query<User> query = currentSession().createQuery(sql, User.class);
		query.setParameter("account", publicAccount);

		return query.getResultList();
	}

	public List<String> getSubscriberAccounts(String publicAccount) {

		String sql = "select account from Subscriber where publicAccount = :account";
		Session session = currentSession();
		Query<String> query = session.createQuery(sql, String.class);
		query.setParameter("account", publicAccount);
		return query.getResultList();
	}

	public boolean hasSubscriberRelation(Subscriber subscriber) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Subscriber.class);
		criteria.add(Restrictions.eq("account", subscriber.getAccount()));
		criteria.add(Restrictions.eq("publicAccount", subscriber.getPublicAccount()));
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString()) > 0;
	}

}
