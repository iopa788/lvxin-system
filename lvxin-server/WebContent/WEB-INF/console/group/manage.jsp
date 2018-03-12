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
     
       function queryMembers(founder,name,groupId)
       {
          $("#group_member_list").empty();
          $("#group_member_list").append("<a   class='list-group-item active' style='border-radius: 0px;'>["+name+"]成员列表</a>");
          $.post("<%=basePath%>/console/group/memberList.action", {groupId:groupId},
			   function(data){
			    $("#group_member_list").show();
			   for(var i = 0;i<data.length;i++)
			   {
			     if(founder==data[i].account)
			     {
			        var line = "<a  class='list-group-item' style='border-radius: 0px;'><img onerror='onUserIconError(this)' width='40px' src='<%=basePath%>/files/user-icon/"+data[i].account+"'/><span style='padding-left: 10px;color:red;'>"+data[i].name+"[群主]</span></a>";
			     }else
			     {
			       	var line = "<a  class='list-group-item' style='border-radius: 0px;'><img onerror='onUserIconError(this)' width='40px' src='<%=basePath%>/files/user-icon/"+data[i].account+"'/><span style='padding-left: 10px;'>"+data[i].name+"</span></a>";
			     }
			     $("#group_member_list").append(line);
			   }
			   
	        });
       
       }
     
    function onUserIconError(obj)
	{
			    obj.src="<%=basePath%>/resource/img/icon_head_default.png";   
	}
		
	function onGroupIconError(obj)
	{
			    obj.src="<%=basePath%>/resource/img/grouphead_normal.png";   
	}	
     </script>
	</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper">
			<div class="lay-main-toolbar">
			</div>

			<div style="float: left;width: 75%;">
				<form action="<%=basePath%>/console/group/list.action" method="post"
					id="searchForm" style="padding: 0px;">
					<input type="hidden" name="currentPage" id="currentPage" value="1"/>
					<table width="100%" class="utable">

						<thead>
							<tr class="tableHeader">

								<th width="15%">
									ID
								</th>
								<th width="15%">
									群徽
								</th>
								<th width="15%">
									名称
								</th>
								<th width="15%">
									创建者
								</th>
								<th width="20%">
									操作
								</th>
							</tr>
							<tr>

								<td style="padding: 2px;">
									<input name="groupId" value="${group.groupId }" type="text" onkeyup="value=value.replace(/[^\d]/g,'')"
										class="form-control" style="width: 90%;display:inline;" />
								</td>
								<td>
								</td>
								<td>
									<input name="name" type="text" value="${group.name }"
										class="form-control" style="width: 90%;display:inline;" />
								</td>
								<td>
									<input name="founder" type="text" value="${group.founder }"
										class="form-control" style="width: 90%;display:inline;" />
								</td>
								<td>
								
								
									<button type="submit" class="btn btn-primary btn-sm">
										<span class="glyphicon glyphicon-search"></span>查询
									</button>
								</td>
							</tr>

						</thead>
						<tbody id="checkPlanList">

							<c:forEach var="group" items="${page.dataList}">
								<tr id="${group.groupId}" style="height: 50px;">

									<td>
										${group.groupId }
									</td>
									<td>
										<img width="40px" height="40px" onerror='onGroupIconError(this)'
											src="<%=basePath%>/files/group-icon/${group.groupId }" />
									</td>

									<td>
										${group.name }
									</td>
									<td>
										${group.founder }
									</td>
 
									<td>
									<div class="btn-group btn-group-xs">
                                      <button type="button" class="btn btn-primary"  style="padding: 5px;" onclick="queryMembers('${group.founder}','${group.name}',${group.groupId})">
                                           <span class="glyphicon glyphicon-list"></span>群成员</button>
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
			
			<div class="list-group" id="group_member_list" style="float: right;width: 24%;display: none;">
			  <a href="#" class="list-group-item active" style="border-radius: 0px;"></a>
			</div>
				
		</div>

		<script>
		       $('#groupMenu').addClass('current');
		</script>
	</body>
</html>
