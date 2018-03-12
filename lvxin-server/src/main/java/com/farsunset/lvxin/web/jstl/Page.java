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
package com.farsunset.lvxin.web.jstl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Page {
	private int count;
	public int size = 10;
	private int currentPage = 1;
	private int countPage;
	private String order;
	private List<?> dataList;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {

		countPage = (count - 1) / size + 1;
		this.count = count;
	}

	public int getFirstResult() {
		return (currentPage - 1) * size;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getCountPage() {

		countPage = (count - 1) / size + 1;
		return countPage;
	}

	public boolean hasNextPage() {
		return currentPage < getCountPage();
	}

	public boolean isFristPage() {
		return currentPage == 1;
	}

	public boolean isLastPage() {
		return currentPage == getCountPage();
	}

	public List<?> getDataList() {
		if (dataList == null)
		{
			return new ArrayList<Object>();
		}
		return dataList;
	}

	public void setDataList(List<?> dataList) {
		this.dataList = dataList;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("count", count);
		data.put("countPage", countPage);
		data.put("currentPage", currentPage);
		data.put("size", size);
		return data;
	}

}
