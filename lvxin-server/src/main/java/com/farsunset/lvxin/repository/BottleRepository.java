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
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.web.jstl.Page;

@SuppressWarnings("unchecked")

@Repository
public class BottleRepository extends HibernateBaseDao<Bottle> {

	public int queryBottleAmount(Bottle model) {
		DetachedCriteria criteria = mapingParam(model);
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	public void queryByPage(Bottle message, Page page) {
		DetachedCriteria criteria = mapingParam(message);
		criteria.addOrder(Order.desc("timestamp"));
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));
	}

	public List<Bottle> queryList(Bottle message) {
		DetachedCriteria criteria = mapingParam(message);
		criteria.addOrder(Order.desc("timestamp"));
		return (List<Bottle>) getHibernateTemplate().findByCriteria(criteria);
	}

	private DetachedCriteria mapingParam(Bottle model) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bottle.class);

		if (StringUtils.isNotEmpty(model.getSender())) {
			criteria.add(Restrictions.eq("sender", model.getSender()));
		}
		if (StringUtils.isNotEmpty(model.getReceiver())) {
			criteria.add(Restrictions.eq("receiver", model.getReceiver()));
		}

		if (null != (model.getType())) {
			criteria.add(Restrictions.eq("type", model.getType()));
		}

		if (StringUtils.isNotEmpty(model.getRegion())) {
			criteria.add(Restrictions.eq("region", model.getRegion()));
		}

		if (null != (model.getStatus())) {
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}

		return criteria;
	}

	public void reset(String gid) {

		Session session = currentSession();
		Query<?> query = session.createQuery("update Bottle set  status=0 , receiver = null where gid= :gid");
		query.setParameter("gid", gid);
		query.executeUpdate();

	}

	public Bottle queryRandom(String account) {
		Session session = currentSession();
		String sql = "select gid from Bottle where sender != :sender and status = 0 and receiver is null";
		Query<String> query = session.createQuery(sql, String.class);
		query.setParameter("sender", account);
		List<String> list = query.getResultList();
		if (list != null && !list.isEmpty()) {
			int random = new Random().nextInt(list.size());
			return get(list.get(random));
		}
		return null;
	}

	public List<Bottle> queryNewList(String account, Page page) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bottle.class);
		criteria.add(Restrictions.eq("status", Bottle.STATUS_NOT_RECEIVED));
		criteria.add(Restrictions.ne("sender", account));
		criteria.addOrder(Order.desc("timestamp"));
		return (List<Bottle>) getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size);
	}
}
