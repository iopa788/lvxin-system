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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.stereotype.Repository;

import com.farsunset.lvxin.exception.IllegalExistCodeException;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.web.jstl.Page;

@SuppressWarnings("unchecked")

@Repository
public class OrganizationRepository extends HibernateBaseDao<Organization> {

	final String INSERT_SQL = "insert into " + Organization.TABLE_NAME + "(code,name,parentCode,sort) values(?,?,?,?)";

	public Page queryPage(Organization model, Page page) {

		DetachedCriteria criteria = mapingParam(model);
		criteria.addOrder(Order.asc("sort"));
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));

		return page;
	}

	public int saveList(final List<Organization> list) {

		Session session = getSessionFactory().openSession();
		return session.doReturningWork(new ReturningWork<Integer>() {
			@Override
			public Integer execute(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(INSERT_SQL);
				con.setAutoCommit(false);
				try {
					int index = 0;
					for (Organization org : list) {
						int i = 1;
						statement.setString(i++, org.getCode());
						statement.setString(i++, org.getName());
						statement.setString(i++, org.getParentCode());
						statement.setInt(i++, org.getSort());

						statement.addBatch();

						if (index % 50 == 0) {
							statement.executeBatch();
						}
						index++;
					}

					statement.executeBatch();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
					throw new IllegalExistCodeException();
				} finally {
					con.commit();
				}
				return list.size();
			}
		});
	}

	public int queryAmount(Organization model) {
		DetachedCriteria criteria = mapingParam(model);
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	private DetachedCriteria mapingParam(Organization model) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Organization.class);
		if (StringUtils.isNotEmpty(model.getName())) {
			criteria.add(Restrictions.like("name", model.getName(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(model.getCode())) {
			criteria.add(Restrictions.eq("code", model.getCode()));
		}
		if (StringUtils.isNotEmpty(model.getParentCode())) {
			criteria.add(Restrictions.eq("parentCode", model.getParentCode()));
		}
		return criteria;
	}

	public List<Organization> queryChildList(String code) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Organization.class);
		criteria.add(Restrictions.eq("parentCode", code));
		criteria.addOrder(Order.asc("sort"));
		List<Organization> list = (List<Organization>) getHibernateTemplate().findByCriteria(criteria);
		return list;
	}

}
