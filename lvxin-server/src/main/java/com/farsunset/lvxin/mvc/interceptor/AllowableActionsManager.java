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
package com.farsunset.lvxin.mvc.interceptor;

import java.util.Properties;

public class AllowableActionsManager {

	private Properties config;

	private static AllowableActionsManager instance;

	private AllowableActionsManager() {
		loadConfig();
	}

	public synchronized static AllowableActionsManager getInstance() {
		if (instance == null) {
			instance = new AllowableActionsManager();
		}
		return instance;
	}

	public void loadConfig() {
		try {
			config = new Properties();
			config.load(AllowableActionsManager.class.getClassLoader().getResourceAsStream("safeactions.properties"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean contains(String key) {
		if (key == null) {
			return false;
		}
		return config.containsKey(key);
	}

	public String get(String key) {
		return config.getProperty(key);
	}

}
