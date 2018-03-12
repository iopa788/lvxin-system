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

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.web.jstl.Page;

@Repository
public class MessageRepository extends HibernateBaseDao<Message> {

	public Page queryMessageList(Message message, Page page) {

		DetachedCriteria criteria = mapingParam(message);
		criteria.addOrder(Order.desc("timestamp"));
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));
		return page;
	}

	@SuppressWarnings("unchecked")
	public List<Message> queryMessageList(Message message) {

		DetachedCriteria criteria = mapingParam(message);
		criteria.addOrder(Order.asc("timestamp"));
		return (List<Message>) getHibernateTemplate().findByCriteria(criteria);
	}

	public int queryMessageAmount(Message model) {
		DetachedCriteria criteria = mapingParam(model);
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	public void queryByPage(Message message, Page page) {
		DetachedCriteria criteria = mapingParam(message);
		criteria.addOrder(Order.desc("timestamp"));
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));
	}

	private DetachedCriteria mapingParam(Message model) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Message.class);

		if (StringUtils.isNotEmpty(model.getSender())) {
			criteria.add(Restrictions.eq("sender", model.getSender()));
		}
		if (StringUtils.isNotEmpty(model.getReceiver())) {
			criteria.add(Restrictions.eq("receiver", model.getReceiver()));
		}

		if (StringUtils.isNotEmpty(model.getAction())) {
			criteria.add(Restrictions.in("action", Arrays.asList(model.getAction().split(","))));
		}
		if (StringUtils.isNotEmpty(model.getStatus())) {
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}

		return criteria;
	}

	public void updateStatus(String mid, String status) {

		Session session = currentSession();
		Query<?> query = session.createQuery("update Message set  status=:status   where mid=:mid");
		query.setParameter("status", status);
		query.setParameter("mid", mid);
		query.executeUpdate();

	}

	public void updateBatchReceived(String account) {

		Session session = currentSession();
		Query<?> query = session
				.createQuery("update Message set  status=:status1   where status=:status2   and receiver=:receiver");
		query.setParameter("status1", Message.STATUS_RECEIVED);
		query.setParameter("status2", Message.STATUS_NOT_RECEIVED);
		query.setParameter("receiver", account);
		query.executeUpdate();

	}

	public void deleteObsoleted() {

		String sql = "delete Message where (action like :action1 or action like :action8 or action = :action444) and status =:status";

		Session session = currentSession();
		Query<?> query = session.createQuery(sql);
		query.setParameter("status", Message.STATUS_RECEIVED);
		query.setParameter("action1", "1__");
		query.setParameter("action8", "8__");
		query.setParameter("action444", Constants.MessageAction.ACTION_444);

		query.executeUpdate();

	}

	public void deleteGroupMessage(List<String> list, Long groupId) {
		Session session = currentSession();
		String sql = "delete from Message where ( sender=:sender and action = :action3 and receiver in( :receivers )) or ( sender in(:senders) and action =:action1 and receiver =:receiver)";
		Query<?> query = session.createQuery(sql);
		query.setParameter("sender", groupId.toString());
		query.setParameter("action3", Constants.MessageAction.ACTION_3);
		query.setParameter("action1", Constants.MessageAction.ACTION_1);
		query.setParameter("receiver", groupId.toString());
		query.setParameterList("receivers", list);
		query.setParameterList("senders", list);
		query.executeUpdate();

	}

	public void deleteGroupMessage(String groupId) {
		Session session = currentSession();
		String sql = "delete from Message where ( sender=:sender and action = :action3) or ( action =:action1 and receiver =:receiver)";
		Query<?> query = session.createQuery(sql);
		query.setParameter("action3", Constants.MessageAction.ACTION_3);
		query.setParameter("action1", Constants.MessageAction.ACTION_1);
		query.setParameter("receiver", groupId);
		query.setParameter("sender", groupId);
		query.executeUpdate();

	}

	public void deleteBottleMessage(Bottle bottle) {
		Session session = currentSession();
		String sql = "delete from Message where (sender=:sender and receiver =:receiver) or  (sender=:receiver and receiver =:sender) and action =:action";
		Query<?> query = session.createQuery(sql);
		query.setParameter("action", Constants.MessageAction.ACTION_700);
		query.setParameter("sender", bottle.getSender());
		query.setParameter("receiver", bottle.getReceiver());
		query.executeUpdate();

	}

	public void deleteByReceiverAndAction(String receiver, String action) {
		Session session = currentSession();
		String sql = "delete from Message where  receiver = :receiver  and action = :action";
		Query<?> query = session.createQuery(sql);
		query.setParameter("action", action);
		query.setParameter("receiver", receiver);
		query.executeUpdate();

	}

	public void deleteBySenderAndAction(String sender, String action) {
		Session session = currentSession();
		String sql = "delete from Message where  sender = :sender  and action = :action";
		Query<?> query = session.createQuery(sql);
		query.setParameter("action", action);
		query.setParameter("sender", sender);
		query.executeUpdate();
	}

	public void delete(String sender, String content, String action) {
		Session session = currentSession();
		String sql = "delete from Message where  content = :content and sender = :sender  and action = :action";
		Query<?> query = session.createQuery(sql);
		query.setParameter("action", action);
		query.setParameter("content", content);
		query.setParameter("sender", sender);
		query.executeUpdate();
	}

	public void deleteCommentRemind(String sender, String receiver, String commentId) {
		Session session = currentSession();
		String sql = "delete from Message where  sender = :sender  and receiver = :receiver and extra = :extra and action in (:action)";
		Query<?> query = session.createQuery(sql);
		query.setParameter("receiver", receiver);
		query.setParameter("sender", sender);
		query.setParameter("extra", commentId);
		query.setParameterList("action",
				new String[] { Constants.MessageAction.ACTION_801, Constants.MessageAction.ACTION_802 });
		query.executeUpdate();
	}

	public void saveList(final List<Message> dataList) {
		final Session session = currentSession();
		session.doReturningWork(new ReturningWork<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				int count = 0;
				String sql = "insert into " + Message.TABLE_NAME
						+ "(mid,sender,receiver,format,title,content,action,status,timestamp) values(?,?,?,?,?,?,?,?,?)";
				PreparedStatement statement = conn.prepareStatement(sql);
				for (Message message : dataList) {
					if (message.isActionMessage()) {
						continue;
					}
					statement.setString(1, message.getMid());
					statement.setString(2, message.getSender());
					statement.setString(3, message.getReceiver());
					statement.setString(4, message.getFormat());
					statement.setString(5, message.getTitle());
					statement.setString(6, message.getContent());
					statement.setString(7, message.getAction());
					statement.setString(8, message.getStatus());
					statement.setLong(9, message.getTimestamp());
					statement.addBatch();
				}
				conn.setAutoCommit(false);
				try {
					count = statement.executeBatch().length;
				} catch (BatchUpdateException e) {
					count = -1;
					e.printStackTrace();
				} finally {
					conn.commit();
				}

				return count;
			}

		});
	}

}
