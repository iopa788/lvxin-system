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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.model.User;

@Repository
public class BatchAddRepository {

	private final String SQL_INSERT_MESSAGE = "insert into " + Message.TABLE_NAME
			+ "(mid,sender,receiver,format,title,content,action,extra,status,timestamp) values(?,?,?,?,?,?,?,?,?,?)";

	private final String SQL_INSERT_ORG = "insert into " + Organization.TABLE_NAME
			+ "(code,name,parentCode,sort) values(?,?,?,?)";

	private final String SQL_INSERT_USER = "insert into " + User.TABLE_NAME
			+ "(account,name,gender,telephone,email,orgCode,password,state) values(?,?,?,?,?,?,?,?)";

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void saveMessage(List<Message> dataList) {
		Session session = (Session) entityManager.getDelegate();
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement statement = connection.prepareStatement(SQL_INSERT_MESSAGE);
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
					statement.setString(8, message.getExtra());
					statement.setString(9, message.getStatus());
					statement.setLong(10, message.getTimestamp());
					statement.addBatch();
				}
				statement.executeBatch();
			}

		});

	}

	@Transactional
	public void saveUser(List<User> dataList) {
		Session session = (Session) entityManager.getDelegate();
		session.doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER);
				for (User user : dataList) {
					statement.setString(1, user.getAccount());
					statement.setString(2, user.getName());
					statement.setString(3, user.getGender());
					statement.setString(4, user.getTelephone());
					statement.setString(5, user.getEmail());
					statement.setString(6, user.getOrgCode());
					statement.setString(7, user.getPassword());
					statement.setString(8, User.STATE_NORMAL);
					statement.addBatch();
				}
				statement.executeBatch();
			}
		});
	}

	@Transactional
	public void saveOrganization(List<Organization> dataList) {
		Session session = (Session) entityManager.getDelegate();
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement statement = connection.prepareStatement(SQL_INSERT_ORG);
				connection.setAutoCommit(false);
				for (Organization org : dataList) {
					statement.setString(1, org.getCode());
					statement.setString(2, org.getName());
					statement.setString(3, org.getParentCode());
					statement.setInt(4, org.getSort());
					statement.addBatch();
				}
				statement.executeBatch();
			}
		});
	}
}
