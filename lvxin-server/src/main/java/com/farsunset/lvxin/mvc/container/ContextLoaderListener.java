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
package com.farsunset.lvxin.mvc.container;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

public class ContextLoaderListener implements ServletContextListener {

	ContextLoader contextLoader;
	protected final Logger logger = Logger.getLogger(ContextLoaderListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if (contextLoader != null)
		{
			contextLoader.closeWebApplicationContext(event.getServletContext());
		}
		ContextHolder.setContext(null);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		if (contextLoader == null) {
			contextLoader = new ContextLoader();
		}

		logger.debug("******************* container start begin ******************************");
		ContextHolder.setContext(contextLoader.initWebApplicationContext(event.getServletContext()));
		logger.debug("******************* container start successfull ************************");

	}

}
