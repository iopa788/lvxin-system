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
	    <script type="text/javascript" src="<%=basePath%>/resource/js/jquery-ui.min.js"></script>
	</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper">
		<div class="lay-main-toolbar">
		 <button type="button" class="btn btn-success" style="margin-top: 5px;"
								onclick="doShowDialog('AddDialog','slide_in_left')">
								<i>+</i>添加
					   </button>
           </div>
		 
				<div>
					<form action="<%=basePath%>/console/publicAccount/list.action" method="post"
						id="searchForm" style="padding: 0px;">
						<input type="hidden" name="currentPage" id="currentPage" value="1"/>
						<table width="100%" class="utable">

							<thead>
								<tr class="tableHeader">
								    <th width="4%">头像</th>
									<th width="15%">账号</th>
									<th width="15%">名称</th>
									<th width="40%">说明</th>
									<th width="20%">操作</th>
								</tr>
							 	<tr>
                                    <td>
									</td>
									<td style=" padding: 2px; ">
										<input name="account" value="${publicAccount.account }" type="text"
											class="form-control"
											style="width: 90%;display:inline;" />
									</td>

									<td>
										<input name="name" type="text" value="${publicAccount.name }"
											class="form-control"
											style="width: 90%;display:inline;" />
									</td>
									<td>
									</td>
									<td >
										<button type="submit" class="btn btn-primary btn-sm">
											<span class="glyphicon glyphicon-search"></span>查询
										</button>
									</td>
								</tr>
							 
							</thead>
							<tbody id="checkPlanList">

								<c:forEach var="publicAccount" items="${page.dataList}">
									<tr id="${publicAccount.account}"  style=" height: 50px;">
										<td>
											 <img width="40px" height="40px" src="<%=basePath%>/files/pub-icon/${publicAccount.account }"/>
										</td>
										<td>
											${publicAccount.account }
										</td>
										<td>
											${publicAccount.name }
										</td>
										<td>
											${publicAccount.description }
										</td>
										<td>
										 
									       <div class="btn-group btn-group-xs">
									          <button type="button" class="btn btn-danger"  style="padding: 5px;" onclick="showPushDialog('${publicAccount.account }')">
									            <span class="glyphicon glyphicon-send"></span> 推送消息</button>
											  <button type="button" class="btn btn-success"  style="padding: 5px;" onclick="showConfigureDialog('${publicAccount.account }')">
											    <span class="glyphicon glyphicon-cog" ></span> 配置管理</button>
											  <button type="button" class="btn btn-primary"  style="padding: 5px;" onclick="showMenuEditDialog('${publicAccount.account }')">
											    <span class="glyphicon glyphicon-list" ></span> 菜单管理</button>
											  <button type="button" class="btn btn-info"  style="padding: 5px;" onclick="showSubscriberDialog('${publicAccount.account }')">
											    <span class="glyphicon glyphicon-user" ></span> 订阅用户</button>
											</div>
										</td>
									</tr>
								</c:forEach>
								 
							</tbody>
							<tfoot>
							<tr>
									<td colspan="5">
										<table:page page="${page}"/>
									</td>
								</tr>
							
							</tfoot>
						</table>
					</form>

				</div>
			</div>
<%@include file="addDialog.jsp"%>
<%@include file="configure.jsp"%>
<%@include file="menuConfig.jsp"%>
<%@include file="pushMessage.jsp"%>
<%@include file="subscriberDialog.jsp"%>

		<script>
		       $('#publicAccountMenu').addClass('current');
		</script>
	</body>
</html>
