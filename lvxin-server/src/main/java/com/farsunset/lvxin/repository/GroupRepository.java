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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.web.jstl.Page;

@Repository
public class GroupRepository extends HibernateBaseDao<Group> {

	public List<Group> getRelevantList(String account) {

		String sql = "select g from Group as g,GroupMember as m where g.groupId = m.groupId and m.account = :account";

		Query<Group> query = currentSession().createQuery(sql, Group.class);
		query.setParameter("account", account);

		return query.getResultList();
	}

	public Page queryPage(Group model, Page page) {

		DetachedCriteria criteria = mapingParam(model);
		criteria.addOrder(Order.desc("groupId"));
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));

		return page;
	}

	public int queryAmount(Group model) {
		DetachedCriteria criteria = mapingParam(model);
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	private DetachedCriteria mapingParam(Group model) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Group.class);
		if (StringUtils.isNotEmpty(model.getName())) {
			criteria.add(Restrictions.like("name", model.getName(), MatchMode.ANYWHERE));
		}
		if (null != (model.getGroupId())) {
			criteria.add(Restrictions.eq("groupId", model.getGroupId()));
		}
		if (StringUtils.isNotEmpty(model.getFounder())) {
			criteria.add(Restrictions.eq("founder", model.getFounder()));
		}
		return criteria;
	}

	public User queryFounder(long groupId) {
		String sql = "select u from Group as g,User as u where g.groupId = :groupId and u.account = g.founder";
		Query<User> query = currentSession().createQuery(sql, User.class);
		query.setParameter("groupId", groupId);
		return query.getSingleResult();
	}

}
