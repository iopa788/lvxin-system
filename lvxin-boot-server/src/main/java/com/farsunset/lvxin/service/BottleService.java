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

import com.farsunset.lvxin.model.Bottle;

public interface BottleService {

	void save(Bottle bottle);

	void update(Bottle bottle);

	void delete(Bottle bottle);

	Bottle queryById(String gid);

	/**
	 * 获取一个随机的未接受的瓶子。排除自己发的
	 * 
	 * @param account
	 * @return
	 */
	Bottle getRandomOne(String account);

}
