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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import com.farsunset.lvxin.model.PublicMenu;

@SuppressWarnings("unchecked")
@Repository
public class PublicMenuRepository extends HibernateBaseDao<PublicMenu> {

	public void deleteSubMenu(String fid) {

		Session session = currentSession();
		String sql = "delete PublicMenu where fid = ? ";
		Query<?> query = session.createQuery(sql);
		query.setParameter(0, fid);
		query.executeUpdate();

	}

	public List<PublicMenu> getPublicMenuList(String account) {

		DetachedCriteria criteria = DetachedCriteria.forClass(PublicMenu.class);
		criteria.add(Restrictions.eq("account", account));
		criteria.addOrder(Order.asc("sort"));
		return (List<PublicMenu>) this.getHibernateTemplate().findByCriteria(criteria);
	}

	public PublicMenu getSingleMenu(String account, String code) {

		DetachedCriteria criteria = DetachedCriteria.forClass(PublicMenu.class);
		criteria.add(Restrictions.eq("code", code));
		criteria.add(Restrictions.eq("account", account));
		List<PublicMenu> list = (List<PublicMenu>) this.getHibernateTemplate().findByCriteria(criteria, 0, 1);

		return list == null || list.isEmpty() ? null : list.get(0);
	}

	public boolean hasExistThisCode(String account, String code) {

		DetachedCriteria criteria = DetachedCriteria.forClass(PublicMenu.class);
		criteria.add(Restrictions.eq("account", account));
		criteria.add(Restrictions.eq("code", code));
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString()) > 0;
	}

	public List<PublicMenu> queryChildList(String fid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PublicMenu.class);
		criteria.add(Restrictions.eq("fid", fid));
		criteria.addOrder(Order.asc("sort"));
		return (List<PublicMenu>) getHibernateTemplate().findByCriteria(criteria);
	}

	public List<PublicMenu> queryRootList(String account) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PublicMenu.class);
		criteria.add(Restrictions.eq("account", account));
		criteria.add(Property.forName("fid").isNull());
		criteria.addOrder(Order.asc("sort"));
		return (List<PublicMenu>) getHibernateTemplate().findByCriteria(criteria);
	}

	public void updateSort(PublicMenu publicMenu) {
		// TODO Auto-generated method stub
		Session session = currentSession();
		String sql = "update PublicMenu set sort = ?  where gid = ? ";
		Query<?> query = session.createQuery(sql);
		query.setParameter(0, publicMenu.getSort());
		query.setParameter(1, publicMenu.getGid());
		query.executeUpdate();

	}

	public int queryChildCount(PublicMenu model) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PublicMenu.class);
		criteria.add(Restrictions.eq("account", model.getAccount()));
		criteria.add(Restrictions.eq("fid", model.getFid()));
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	public int queryRootCount(PublicMenu model) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PublicMenu.class);
		criteria.add(Restrictions.eq("account", model.getAccount()));
		criteria.add(Restrictions.eq("type", PublicMenu.ACTION_ROOT));
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	public int getMaxSort(PublicMenu publicMenu) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PublicMenu.class);
		criteria.add(Restrictions.eq("account", publicMenu.getAccount()));
		if (publicMenu.getFid() != null) {
			criteria.add(Restrictions.eq("fid", publicMenu.getFid()));
		}
		criteria.setProjection(Projections.projectionList().add(Projections.max("sort")));
		List<?> list = getHibernateTemplate().findByCriteria(criteria);

		Object maxSort = list.get(0);

		return maxSort == null ? 0 : Integer.valueOf(maxSort.toString());
	}
}
