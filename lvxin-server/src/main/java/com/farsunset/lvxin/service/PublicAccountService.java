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

import org.springframework.web.multipart.MultipartFile;

import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.web.jstl.Page;

public interface PublicAccountService {

	void update(PublicAccount user);

	void queryPage(PublicAccount publicAccount, Page page);

	PublicAccount queryById(String paccount);

	void save(PublicAccount user);

	void delete(String account);

	List<PublicAccount> queryListByUserAccount(String account);

	List<PublicAccount> queryAll();

	void updateLogo(String account,MultipartFile file);
}
