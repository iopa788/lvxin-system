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

import com.farsunset.lvxin.model.Article;
import com.farsunset.lvxin.model.MomentRule;
import com.farsunset.lvxin.web.jstl.Page;

@Repository
public class ArticleRepository extends HibernateBaseDao<Article>

{

	public Page queryByPage(Article article, Page page) {
		DetachedCriteria criteria = mapingParam(article);
		criteria.addOrder(Order.desc("timestamp"));
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));
		return page;
	}

	public void queryRelevantByPage(String account, Page page) {

		Session session = currentSession();

		String sql = "select a  from Article as a where a.account not in (select account from MomentRule  where otherAccount =:account and account = a.account and type = :type0) and a.account not in (select otherAccount from MomentRule  where account =:account and otherAccount = a.account and type = :type1) order by a.timestamp desc";
		Query<Article> query = session.createQuery(sql, Article.class);
		query.setParameter("account", account);
		query.setParameter("type0", MomentRule.TYPE_0);
		query.setParameter("type1", MomentRule.TYPE_1);
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.size);
		page.setDataList(query.getResultList());

	}

	public Page queryByPage(String account, Page page) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		criteria.addOrder(Order.desc("timestamp"));
		criteria.add(Restrictions.eq("account", account));
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));

		return page;

	}

	public int queryAmount(Article article) {
		DetachedCriteria criteria = mapingParam(article);
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	public long queryRelevantAmount(String account) {

		Session session = currentSession();

		String sql = "select count(gid)  from Article as a where a.account not in (select account from MomentRule  where otherAccount =:account and account = a.account and type = :type0) and a.account not in (select otherAccount from MomentRule  where account =:account and otherAccount = a.account and type = :type1) order by a.timestamp desc";
		Query<Long> query = session.createQuery(sql, Long.class);
		query.setParameter("account", account);
		query.setParameter("type0", MomentRule.TYPE_0);
		query.setParameter("type1", MomentRule.TYPE_1);

		return query.getSingleResult();
	}

	private DetachedCriteria mapingParam(Article article) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);

		if (!StringUtils.isEmpty(article.getAccount())) {
			criteria.add(Restrictions.eq("account", article.getAccount()));
		}
		if (!StringUtils.isEmpty(article.getType())) {
			criteria.add(Restrictions.eq("type", article.getType()));
		}

		return criteria;
	}

	public int queryAmount(String account) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		criteria.add(Restrictions.eq("account", account));
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	@SuppressWarnings("unchecked")
	public List<Article> queryByAccount(String account) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		criteria.add(Restrictions.eq("account", account));
		return (List<Article>) getHibernateTemplate().findByCriteria(criteria);
	}

	public void deleteByAccount(String account) {
		Session session = currentSession();
		String sql = "delete  from Article   where  account  = :account";
		Query<?> query = session.createQuery(sql);
		query.setParameter("account", account);
		query.executeUpdate();
	}

}
