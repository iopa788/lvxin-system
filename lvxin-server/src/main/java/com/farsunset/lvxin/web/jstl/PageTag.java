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

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class PageTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Page page;

	@Override
	public int doStartTag() {
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() {

		JspWriter out = pageContext.getOut();
		try {

			out.println("<div style='float: left;line-height: 40px;margin-left: 10px;'>共" + page.getCount() + "条记录,"
					+ page.getCountPage() + "页</div>");
			if (page.getCountPage() == 1) {
				return Tag.EVAL_PAGE;
			}
			out.println("<ul class=\"pagination\" style='margin: 5px 0;'>");

			if (!page.isFristPage()) {
				out.println("<li style='cursor: pointer;'><a onclick='gotoPage(1)'>&laquo;</a></li>");
				out.println("<li style='cursor: pointer;'><a onclick='gotoPage(" + (page.getCurrentPage() - 1)
						+ ")'>上一页</a></li>");
			} else {
				out.println("<li  class=\"disabled\"><a>&laquo;</a></li>");
			}
			for (int i = page.getCurrentPage() - 5; i < page.getCurrentPage() + 6 && i <= page.getCountPage(); i++) {
				if (i < 1) {
					continue;
				}
				if (i == page.getCurrentPage()) {
					out.println("<li class=\"active\"><a >" + i + "</a></li>");
				} else {
					out.println("<li style='cursor: pointer;'><a onclick='gotoPage(" + i + ")'>" + i + "</a></li>");
				}

			}
			if (!page.isLastPage()) {
				out.println("<li style='cursor: pointer;'><a onclick='gotoPage(" + (page.getCurrentPage() + 1)
						+ ")'>下一页</a></li>");
				out.println(" <li style='cursor: pointer;'><a onclick='gotoPage(" + page.getCountPage()
						+ ")'>&raquo;</a></li>");
			} else {
				out.println(" <li class=\"disabled\"><a>&raquo;</a></li>");

			}
			out.println(
					"<li><input onkeyup=\"value=value.replace(/[^\\d]/g,'')\" type=\"number\" id='GoPageNumber' style='padding:6px;width: 60px;height: 31px;' class=\"input-pagenumber form-control\" ><a style='float: right;cursor: pointer;' onclick=\"gotoPage($('#GoPageNumber').val())\">GO</a></li>");
			out.println("</ul>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Tag.EVAL_PAGE;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
