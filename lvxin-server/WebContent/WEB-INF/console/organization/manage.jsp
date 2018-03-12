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

     <script type="text/javascript">
      function doDelete(code)
		  {
		     var setting = {hint:"删除后无法恢复,确定删除这个组织吗?",
		                    onConfirm:function(){
		                      showProcess('正在删除，请稍候......');
		                      $.post("<%=basePath%>/console/organization/delete.action", {code:code},
							  function(data){
							      hideProcess();
							      doHideConfirm();
							      if(data.code == 200){
							        $('#'+code).fadeOut().fadeIn().fadeOut();
							        showSTip("删除成功");
							      }
					              if(data.code == 403){
					                 showETip("不能删除，子组织不为空");
							      }
					              if(data.code == 405){
						             showETip("不能删除，还存在部门成员");
								  }
						              
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
				     <button type="button" class="btn btn-primary"  
									onclick="doShowDialog('ImportDialog','slide_in_left')">
									批量导入
					 </button>
					 
					 <button type="button" class="btn btn-primary"  
									onclick="window.location.href='<%=basePath%>/template/org_template.xlsx';">
									模板下载
					 </button>
					 <button type="button" class="btn btn-success"  
									onclick="doShowDialog('AddDialog','slide_in_left')">
									<i class="">+</i>添加
					 </button>
			     </div>
			       
			</div>

				<form action="<%=basePath%>/console/organization/list.action" method="post"
					id="searchForm" style="padding: 0px;">
					<input type="hidden" name="currentPage" id="currentPage" value="1"/>
					<table width="100%" class="utable">

						<thead>
							<tr class="tableHeader">

								<th width="20%">
									组织编号
								</th>
								<th width="20%">
									名称
								</th>
								<th width="20%">
									上级编号
								</th>
								<th width="20%">
									排序
								</th>
								<th width="20%">
									操作
								</th>
							</tr>
							<tr>

								<td style="padding: 2px;">
									<input name="code" value="${org.code}" type="text"  
										class="form-control" style="width: 90%;display:inline;" />
								</td>
								<td>
									<input name="name" type="text" value="${org.name }"
										class="form-control" style="width: 90%;display:inline;" />
								</td>
								<td>
									<input name="parentCode" type="text" value="${org.parentCode }"
										class="form-control" style="width: 90%;display:inline;" />
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
						<tbody>

							<c:forEach var="org" items="${page.dataList}">
								<tr id="${org.code}" style="height: 50px;">

									<td>
										${org.code }
									</td>
									 
									<td>
										${org.name }
									</td>
									<td>
										${org.parentCode }
									</td>
									<td>
										${org.sort }
									</td>
									<td>
									 <div class="btn-group btn-group-xs">
											  <button type="button" class="btn btn-primary"  style="padding: 5px;" onclick="showEditDialog(${org})">
											  <span class="glyphicon glyphicon-edit"></span>修改</button>
											  <button type="button" class="btn btn-danger"  style="padding: 5px;" onclick="doDelete('${org.code}')">
											  <span class="glyphicon glyphicon-trash"></span>删除</button>
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
<%@include file="importDialog.jsp"%>
<%@include file="addDialog.jsp"%>
<%@include file="editDialog.jsp"%>
		<script>
		       $('#orgMenu').addClass('current');
		</script>
	</body>
</html>
