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

import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.web.jstl.Page;

@Repository
public class CommentRepository extends HibernateBaseDao<Comment>

{

	public Page queryByPage(Comment comment, Page page) {
		DetachedCriteria criteria = mapingParam(comment);
		criteria.addOrder(Order.desc("timestamp"));
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));
		return page;
	}

	public int queryAmount(Comment comment) {
		DetachedCriteria criteria = mapingParam(comment);
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	private DetachedCriteria mapingParam(Comment comment) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
		if (!StringUtils.isEmpty(comment.getArticleId())) {
			criteria.add(Restrictions.eq("articleId", comment.getArticleId()));
		}

		if (!StringUtils.isEmpty(comment.getAccount())) {
			criteria.add(Restrictions.eq("account", comment.getAccount()));
		}
		if (!StringUtils.isEmpty(comment.getType())) {
			criteria.add(Restrictions.eq("type", comment.getType()));
		}
		return criteria;
	}

	public void deleteByArticleId(String gid) {
		Session session = currentSession();
		String sql = "delete   Comment   where articleId = :articleId";
		Query<?> query = session.createQuery(sql);
		query.setParameter("articleId", gid);
		query.executeUpdate();

	}

	public void deleteByAccount(String account) {
		Session session = currentSession();
		String sql = "delete   Comment   where account = :account";
		Query<?> query = session.createQuery(sql);
		query.setParameter("account", account);
		query.executeUpdate();

	}

	public List<Comment> queryList(String gid) {

		String sql = "from  Comment   where articleId = :articleId order by timestamp";
		Query<Comment> query = currentSession().createQuery(sql, Comment.class);
		query.setParameter("articleId", gid);
		return query.getResultList();
	}
}
