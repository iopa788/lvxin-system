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

import java.util.List;

import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.web.jstl.Page;

public interface OrganizationService {

	void add(Organization group);

	int saveBatch(List<Organization> list);

	void update(Organization group);

	void delete(String code);

	List<Organization> queryList();

	List<Organization> queryChildList(String code);

	Organization queryByCode(String code);

	void queryPage(Organization model, Page page);
}
