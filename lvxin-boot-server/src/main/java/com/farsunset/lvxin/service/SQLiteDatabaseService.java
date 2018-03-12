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
package com.farsunset.lvxin.service;

import java.io.File;

import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.model.User;

public interface SQLiteDatabaseService {
	void initDatabase();

	void add(User user);

	void update(User user);

	void delete(User user);

	void add(Organization org);

	void update(Organization org);

	void delete(Organization user);

	void createDatabase(boolean notify);

	void createUserDatabase();

	void createOrgDatabase();

	boolean isDbFileReady();

	File getDatabaseFile();
}
