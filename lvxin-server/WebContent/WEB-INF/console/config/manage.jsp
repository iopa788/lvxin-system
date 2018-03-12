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
		<script>
		
		
		
		  function doDelete(id)
		  {
		     var setting = {hint:"删除后无法恢复,确定删除这个配置吗?",
		                    onConfirm:function(){
		                      $.post("<%=basePath%>/console/config/delete.action", {gid:id},
							  function(data){
							      hideProcess();
					              $('#'+id).fadeOut().fadeIn().fadeOut();
					              doHideConfirm();
					              showSTip("删除成功");
						      });
		                     
		                    }};
		     doShowConfirm(setting);
		  }
		  
		   
		</script>
	</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper">
		
		<div class="lay-main-toolbar">
		   <div class="btn-group" style="margin-top:5px;">
			     <button type="button" class="btn btn-success"  
								onclick="doShowDialog('AddVersionDialog','slide_in_left')">
								<i>+</i>新版本
				 </button>
				 <button type="button" class="btn btn-success"  
								onclick="doShowDialog('AddDialog','slide_in_left')">
								<i>+</i>添加
				 </button>
			</div>
		 
			</div>

			<div>
			 
					<form action="<%=basePath%>/console/config/list.action" method="post"
						id="searchForm" style="padding: 0px;">
						<input type="hidden" name="currentPage" id="currentPage" value="1" />
						<table width="100%" class="utable">

							<thead>
								<tr class="tableHeader">
								    <th width="10%">域</th>
									<th width="10%">键</th>
									<th width="50%">值</th>
									<th width="10%">操作</th>
								</tr>
							 
							</thead>
							<tbody id="checkPlanList">

								<c:forEach var="config" items="${list}">
									<tr id="${config.gid}">
										<td>
										${config.domain }
										</td>
										<td>
											${config.key }
										</td>
										<td>
											${config.value }
										</td>
									 
										 
										<td>
										
									 
									       <div class="btn-group btn-group-xs">
											  <button type="button" class="btn btn-primary"  style="padding: 5px;" onclick="showEditDialog(${config})">
									 <span class="glyphicon glyphicon-edit"></span>修改</button>
											  <button type="button" class="btn btn-danger"  style="padding: 5px;" onclick="doDelete('${config.gid}')">
											  <span class="glyphicon glyphicon-trash"></span>删除</button>
											</div>
								 
											
	 
										</td>
									</tr>
								</c:forEach>
								 
							</tbody>
							 
						</table>
					</form>

				</div>
			</div>
			
			<%@include file="addDialog.jsp"%>
			<%@include file="newVersionDialog.jsp"%>
            <%@include file="editDialog.jsp"%>
		<script>
		       $('#configMenu').addClass('current');
		</script>
	</body>
</html>
