<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/table.tld" prefix="table"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<title>侣信管理系统</title>
        <%@include file="/resources.jsp"%>
	</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper">
			<div class="lay-main-toolbar">
			</div>

			<div>
				<form action="<%=basePath%>/console/feedback/list.action" method="post"
					id="searchForm" style="padding: 0px;">
					<input type="hidden" name="currentPage" id="currentPage" value="1" />
					<table width="100%" class="utable">

						<thead>
							<tr class="tableHeader">

								 
								<th width="10%">
									反馈者
								</th>
								<th width="10%">
									应用版本
								</th>
								<th width="10%">
									系统版本
								</th>
								<th width="10%">
									设备型号
								</th>
								<th width="50%">
									反馈内容
								</th>
								<th width="10%">
									反馈时间
								</th>
								<th width="5%">
									操作
								</th>
							</tr>
							<tr>

								
								<td style="padding: 2px;">
									<input name="account" type="text" value="${feedback.account }" class="form-control" style="width: 100%; font-size: 14px;" />
								</td>
								<td style="padding: 2px;">
								    <input name="appVersion" type="text" value="${feedback.appVersion }" class="form-control" style="width: 100%; font-size: 14px;" />
								</td>
								<td style="padding: 2px;">
								    <input name="sdkVersion" type="text" value="${feedback.sdkVersion }" class="form-control" style="width: 100%; font-size: 14px;" />
								</td>
								<td>
								</td>
								<td>
								</td>
								<td>
								</td>
								<td>
									<button type="submit" class="btn btn-primary btn-sm">
										<span class="glyphicon glyphicon-search"></span>查询
									</button>
								</td>
							</tr>

						</thead>
						<tbody id="checkPlanList">

							<c:forEach var="feedback" items="${page.dataList}">
								<tr id="${feedback.gid}" style="height: 50px;">

									<td>
										${feedback.account }
									</td>
									 <td>

										${feedback.appVersion }
									</td>
									 <td>

										${feedback.sdkVersion }
									</td>
									<td>

										${feedback.deviceModel }
									</td>
									<td>

										${feedback.content }
									</td>
									<td>
										<table:datetime timestamp="${feedback.timestamp }"/>
									</td>

									<td>
									</td>
								</tr>
							</c:forEach>

						</tbody>
						<tfoot>
							<tr>
								<td colspan="7">
									<table:page page="${page}"/>
								</td>
							</tr>

						</tfoot>
					</table>
				</form>

			</div>
			 
		</div>

		<script>
		       $('#feedbackMenu').addClass('current');
		</script>
	</body>
</html>
