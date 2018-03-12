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
package com.farsunset.lvxin.util;

import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.dto.Page;

public class PageCompat {

	public static BaseResult transform(org.springframework.data.domain.Page<?> dto) {
		BaseResult result = new BaseResult();
		Page page = new Page();
		page.setCount(dto.getTotalElements());
		page.setCountPage(dto.getTotalPages());
		page.setSize(dto.getNumberOfElements());
		page.setCurrentPage(dto.getNumber());
		result.page = page;
		result.dataList = dto.getContent();
		return result;
	}

}
