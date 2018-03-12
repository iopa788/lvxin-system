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
package com.farsunset.lvxin.service.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.cim.push.BroadcastMessagePusher;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.repository.OrganizationRepository;
import com.farsunset.lvxin.repository.UserRepository;
import com.farsunset.lvxin.service.SQLiteDatabaseService;
import com.farsunset.lvxin.util.Constants;

@Service
public class SQLiteDatabaseServiceImpl implements SQLiteDatabaseService {
	private final Logger log = Logger.getLogger(SQLiteDatabaseServiceImpl.class.getName());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private BroadcastMessagePusher broadcastMessagePusher;
	@Autowired
	private LocalManagerServiceImpl localManagerServiceImpl;

	private final String DBNAME = "base.db";

	private final String BUCKET = "sqlite";

	private File dbFile;
	private Connection connection;
	private final String ORG_INSERT_SQL = "insert into " + Organization.TABLE_NAME
			+ "(code,name,parentCode,sort) values(?,?,?,?)";

	private final String ORG_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + Organization.TABLE_NAME
			+ "(code TEXT PRIMARY KEY,name TEXT, parentCode TEXT, sort INTEGE)";

	private final String ORG_CLEAR_SQL = "delete from " + Organization.TABLE_NAME;

	private final String DELETE_ORG_SQL = "delete from " + Organization.TABLE_NAME + " where code = ?";

	private final String UPDATE_ORG_SQL = "update  " + Organization.TABLE_NAME
			+ " set name = ?,parentCode = ?,sort = ? where code = ?";

	private final String DELETE_USER_SQL = "delete from " + User.TABLE_NAME + " where account = ?";

	private final String UPDATE_USER_SQL = "update  " + User.TABLE_NAME
			+ " set name = ?,telephone = ?,orgCode = ?,email = ?,grade = ?,gender = ?,motto = ?,feature = ?,online = ?"
			+ " where account = ?";

	private final String INSERT_SQL = "insert into " + User.TABLE_NAME
			+ "(account,name,telephone,orgCode,email,grade,gender,motto,feature,online) values(?,?,?,?,?,?,?,?,?,?)";

	private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + User.TABLE_NAME
			+ "(account varchar(64) PRIMARY KEY,name varchar(64), telephone varchar(20), orgCode varchar(64), email varchar(64), grade INTEGE, gender varchar(1),online varchar(1) , motto TEXT,feature varchar(32)  )";

	private final String USER_CLEAR_SQL = "delete from " + User.TABLE_NAME;

	@Override
	public void initDatabase() {
		try {
			Class.forName("org.sqlite.JDBC");
			dbFile = localManagerServiceImpl.getFile(BUCKET, DBNAME);
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
		} catch (Exception e) {
			log.severe(e.getMessage());
		}

		createTable();

		createDatabase(false);
	}

	@Override
	public boolean isDbFileReady() {
		return dbFile.exists();
	}

	@Override
	public File getDatabaseFile() {
		return dbFile;
	}

	@Override
	public void add(User user) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(INSERT_SQL);
			connection.setAutoCommit(true);
			setStatementParams(statement, user);
			statement.execute();
		} catch (SQLException e) {
			log.severe(e.getMessage());
		}

		notification();
	}

	@Override
	public void delete(User user) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(DELETE_USER_SQL);
			connection.setAutoCommit(true);
			statement.setString(1, user.getAccount());
			statement.execute();
		} catch (SQLException e) {
			log.severe(e.getMessage());
		}

		notification();
	}

	@Override
	public void update(User user) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(UPDATE_USER_SQL);
			connection.setAutoCommit(true);
			int i = 1;
			statement.setString(i++, user.getName());
			statement.setString(i++, user.getTelephone());
			statement.setString(i++, user.getOrgCode());
			statement.setString(i++, user.getEmail());
			statement.setInt(i++, user.getGrade() == null ? 0 : user.getGrade());
			statement.setString(i++, user.getGender());
			statement.setString(i++, user.getMotto());
			statement.setString(i++, user.getFeature());
			statement.setString(i++, user.getOnline());
			statement.setString(i, user.getAccount());
			statement.execute();
		} catch (SQLException e) {
			log.severe(e.getMessage());
		}

		notification();
	}

	@Override
	public void add(Organization org) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(ORG_INSERT_SQL);
			connection.setAutoCommit(true);
			setStatementParams(statement, org);
			statement.execute();
		} catch (SQLException e) {
			log.severe(e.getMessage());
		}

		notification();
	}

	@Override
	public void update(Organization org) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(UPDATE_ORG_SQL);
			connection.setAutoCommit(true);
			int i = 1;
			statement.setString(i++, org.getName());
			statement.setString(i++, org.getParentCode());
			statement.setInt(i++, org.getSort());
			statement.setString(i, org.getCode());
			statement.execute();
		} catch (SQLException e) {
			log.severe(e.getMessage());
		}

		notification();
	}

	@Override
	public void delete(Organization org) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(DELETE_ORG_SQL);
			connection.setAutoCommit(true);
			statement.setString(1, org.getCode());
			statement.execute();
		} catch (SQLException e) {
			log.severe(e.getMessage());
		}

		notification();
	}

	@Override
	public void createDatabase(boolean notify) {
		clearTable();
		batchInsertOrg(organizationRepository.findAll());
		batchInsertUser(userRepository.findAll());
		if (notify) {
			notification();
		}
	}

	@Override
	public void createUserDatabase() {
		executeSQL(USER_CLEAR_SQL);
		batchInsertUser(userRepository.findAll());
		notification();
	}

	@Override
	public void createOrgDatabase() {
		executeSQL(ORG_CLEAR_SQL);
		batchInsertOrg(organizationRepository.findAll());
		notification();
	}

	private void setStatementParams(PreparedStatement statement, User user) throws SQLException {
		int i = 1;
		statement.setString(i++, user.getAccount());
		statement.setString(i++, user.getName());
		statement.setString(i++, user.getTelephone());
		statement.setString(i++, user.getOrgCode());
		statement.setString(i++, user.getEmail());
		statement.setInt(i++, user.getGrade() == null ? 0 : user.getGrade());
		statement.setString(i++, user.getGender());
		statement.setString(i++, user.getMotto());
		statement.setString(i++, user.getFeature());
		statement.setString(i, user.getOnline());
	}

	private void batchInsertUser(List<User> list) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(INSERT_SQL);
			connection.setAutoCommit(false);
			for (User user : list) {
				setStatementParams(statement, user);
				statement.addBatch();
			}
			statement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			log.severe(e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				log.severe(e.getMessage());
			}
		}
	}

	private void setStatementParams(PreparedStatement statement, Organization org) throws SQLException {
		int i = 1;
		statement.setString(i++, org.getCode());
		statement.setString(i++, org.getName());
		statement.setString(i++, org.getParentCode());
		statement.setInt(i, org.getSort());
	}

	private void batchInsertOrg(List<Organization> list) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(ORG_INSERT_SQL);
			connection.setAutoCommit(false);
			for (Organization org : list) {
				setStatementParams(statement, org);
				statement.addBatch();
			}
			statement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				log.severe(e.getMessage());
			}
			log.severe(e.getMessage());
		}

	}

	private void clearTable() {
		executeSQL(USER_CLEAR_SQL, ORG_CLEAR_SQL);
	}

	private void createTable() {
		executeSQL(CREATE_TABLE, ORG_CREATE_TABLE);
	}

	private void executeSQL(String... sqlArray) {
		Statement statement;
		try {
			statement = connection.createStatement();
			for (String sql : sqlArray) {
				statement.execute(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 基础数据变更，发送通知给所有在线用户更新数据
	 */
	private void notification() {
		Message message = new Message();
		message.setAction(Constants.MessageAction.ACTION_998);
		message.setSender(Constants.SYSTEM);
		broadcastMessagePusher.pushOnline(message);
	}

}
