<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		                      $.post("<%=basePath%>/console/host/delete.action", {ip:id},
							  function(data){
							      showSTip("删除成功");
					              $('#'+id).fadeOut().fadeIn().fadeOut();
					              doHideConfirm();
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
                   <div class="btn-group">
					   <button type="button" class="btn btn-success" style="margin-top: 5px;" onclick="doShowDialog('AddDialog','slide_in_left')">
								<i>+</i>添加
					   </button>
					</div>
           </div>
                
			<div>
					<form action="<%=basePath%>/console/host/list.action" method="post"
						id="searchForm" style="padding: 0px;">
						<input type="hidden" name="currentPage" id="currentPage" value="1"/>
						<table width="100%" class="utable">

							<thead>
								<tr class="tableHeader">
								    <th width="10%">IP</th>
									<th width="50%">说明</th>
									<th width="20%">操作</th>
								</tr>
							 
							</thead>
							<tbody>

								<c:forEach var="host" items="${list}">
									<tr id="${host.ip}">
									<td>
										${host.ip }
										</td>
										<td>
										${host.descrption }
										</td>
									 
										<td>
											<div class="btn-group btn-group-xs">
											  <button type="button" class="btn btn-danger"  style="padding: 5px;" onclick="doDelete('${host.ip}')">删除</button>
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
		<script>
		       $('#serverMenu').addClass('current');
		</script>
	</body>
</html>
