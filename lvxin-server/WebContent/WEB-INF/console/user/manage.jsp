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
		      function doDelete(account)
				  {
				     var setting = {hint:"删除后无法恢复,确定删除这个用户吗?",
				                    onConfirm:function(){
				                      showProcess('正在删除，请稍候......');
				                      $.post("<%=basePath%>/console/user/delete.action", {account:account},
									  function(data){
									      hideProcess();
									      doHideConfirm();
									      if(data == 200){
									        $('#'+account).fadeOut().fadeIn().fadeOut();
									        showSTip("删除成功");
									      }
							              
								      });
				                     
				                    }};
				     doShowConfirm(setting);
				  }
	 function doResetPassword(account)
	 {
			var setting = {hint:"确定重置这个帐号的密码吗?",
				           onConfirm:function(){
				               showProcess('正在处理，请稍候......');
				               $.post("<%=basePath%>/console/user/resetPassword.action", {account:account},
								 function(data){
									  hideProcess();
									  doHideConfirm();
									  if(data.code == 200){
									        showSTip("重置成功");
								      }
							     });
			              }};
			doShowConfirm(setting);
	}	
	
	 function toogleState(obj,account,state)
	 {
			var setting = {hint:"确定切换该帐号的状态吗?",
				           onConfirm:function(){
				               showProcess('正在处理，请稍候......');
				               $.post("<%=basePath%>/console/user/toogleState.action", {account:account,state:state},
								 function(data){
									  hideProcess();
									  doHideConfirm();
									  if(data.code == 200){
									        if(state==1){
									               $(obj).parent().find(".disenable-switch").hide();
									               $(obj).parent().find(".enable-switch").fadeIn();
									               $("#"+account).find(".state").text("禁用");
									               showSTip("禁用成功");
									        } 
									        if(state==0){
									        	   $(obj).parent().find(".enable-switch").hide();
									               $(obj).parent().find(".disenable-switch").fadeIn();
									               $("#"+account).find(".state").text("正常");
									               showSTip("启用成功");
									        }
								      }
							     });
			              }};
			doShowConfirm(setting);
	}			  
	function onUserIconError(obj)
	{
		 obj.src="<%=basePath%>/resource/img/icon_head_default.png";   
	}
          </script>
	</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper">
		<div class="lay-main-toolbar">
		<div class="btn-group" style="margin-top:5px; ">
			     <button type="button" class="btn btn-primary"  
								onclick="doShowDialog('ImportDialog','slide_in_left')">
								批量导入
				 </button>
				 <button type="button" class="btn btn-primary"  
								onclick="window.location.href='<%=basePath%>/template/user_template.xlsx';">
								模板下载
				 </button>
				 <button type="button" class="btn btn-success"  
								onclick="doShowDialog('AddDialog','slide_in_left')">
								<i>+</i>添加
				 </button>
				 </div>
			</div>

		 
				<div>
					<form action="<%=basePath%>/console/user/list.action" method="post"
						id="searchForm" style="padding: 0px;">
						<input type="hidden" name="currentPage" id="currentPage" value="1" />
						<table width="100%" class="utable">

							<thead>
								<tr class="tableHeader">
								    <th width="4%">头像</th>
									<th width="15%">账号</th>
									<th width="10%">名称</th>
									<th width="10%">性别</th>
									<th width="10%">电话号码</th>
									<th width="10%">邮箱</th>
									<th width="10%">组织编号</th>
									<th width="10%">状态</th>
									<th width="10%">操作</th>
								</tr>
							 	<tr>
                                    <td>
									</td>
									<td style="padding: 2px;">
										<input name="account" value="${user.account }" type="text"
											class="form-control"
											style="width: 90%;display:inline;" />
									</td>

									<td>
										<input name="name" type="text" value="${user.name }"
											class="form-control"
											style="width: 90%;display:inline;" />
									</td>
									<td>
									<select name="gender" id="mstatus" class="form-control" style="width:90%;display:inline;">
									 <option />
									 <option value="1" <c:if test="${user.gender  eq '1'}"> selected="selected" </c:if> >男</option>
									 <option value="0" <c:if test="${user.gender  eq '0'}"> selected="selected" </c:if>>女</option>
									</select>
									</td>
									<td>
									<input name="telephone" value="${user.telephone }" type="text"
											class="form-control"
											style="width: 90%;display:inline;" />
									</td>
									<td>
									<input name="email" value="${user.email }" type="text"
											class="form-control"
											style="width: 90%;display:inline;" />
									</td>
									<td>
									<input name="orgCode" value="${user.orgCode }" type="text"
											class="form-control"
											style="width: 90%;display:inline;" />
									</td>
									<td>
									<select name="state" id="mstatus" class="form-control" style="width: 90%;display:inline;">
									 <option />
									 <option value="1" <c:if test="${user.state  eq '1'}"> selected="selected"</c:if>>禁用</option>
									 <option value="0" <c:if test="${user.state  eq '0'}"> selected="selected"</c:if>>正常</option>
									</select>
									</td>
									<td >
										<button type="submit" class="btn btn-primary btn-sm">
											<span class="glyphicon glyphicon-search"></span>查询
										</button>
									</td>
								</tr>
							 
							</thead>
							<tbody id="checkPlanList">

								<c:forEach var="user" items="${page.dataList}">
									<tr id="${user.account}"  style=" height: 50px;">
										<td>
											 <img onerror='onUserIconError(this)' width="40px" height="40px" src="<%=basePath%>/files/user-icon/${user.account }"/>
										</td>
										<td>
											${user.account }
										</td>
										<td>
											${user.name }
										</td>
										<td>
											<c:if test="${user.gender eq '0' }">女</c:if>
											<c:if test="${user.gender eq '1' }">男</c:if>
										</td>
									 <td>
											${user.telephone }
										</td>
										<td>
											${user.email }
										</td>
										<td>
											${user.orgCode } 
										</td>
										<td class="state">
										    <c:if test="${user.state eq null || user.state eq '0' }">正常</c:if>
											<c:if test="${user.state eq '1' }">禁用</c:if>
										</td>
										<td>
										
										  <div class="btn-group btn-group-xs">
											  <button type="button" class="btn btn-primary"  style="padding: 5px;" onclick="showEditDialog(${user})">
											   <span class="glyphicon glyphicon-edit"></span>修改
											  </button>
											  <button type="button" class="btn btn-warning"  style="padding: 5px;" onclick="doResetPassword('${user.account}')">
											  <span class="glyphicon glyphicon-lock"></span>重置</button>
											  <c:if test="${user.state eq null || user.state eq '0' }">
                                                  <button type="button" class="btn btn-danger disenable-switch"  style="padding: 5px;" onclick="toogleState(this,'${user.account}',1)">
											      <span class="glyphicon glyphicon-edit"></span>禁用
											      </button>
											      <button type="button" class="btn btn-success enable-switch"  style="padding: 5px;display:none;" onclick="toogleState(this,'${user.account}',0)">
											      <span class="glyphicon glyphicon-edit"></span>启动
											      </button>
                                              </c:if>
                                              <c:if test="${user.state eq '1' }">
                                                 <button type="button" class="btn btn-danger disenable-switch"  style="padding: 5px;display:none;" onclick="toogleState(this,'${user.account}',1)">
											      <span class="glyphicon glyphicon-edit"></span>禁用
											      </button>
											      <button type="button" class="btn btn-success enable-switch"  style="padding: 5px;" onclick="toogleState(this,'${user.account}',0)">
											      <span class="glyphicon glyphicon-edit"></span>启用
											      </button>
                                              </c:if>
									      </div>
										</td>
									</tr>
								</c:forEach>
								 
							</tbody>
							<tfoot>
							<tr>
									<td colspan="9">
										<table:page page="${page}"/>
									</td>
								</tr>
							
							</tfoot>
						</table>
					</form>

				</div>
			</div>
<%@include file="importDialog.jsp"%>
<%@include file="addDialog.jsp"%>
<%@include file="editDialog.jsp"%>
		<script>
		       $('#userMenu').addClass('current');
		</script>
	</body>
</html>
