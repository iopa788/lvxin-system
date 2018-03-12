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
function   showDetailed(gid)
{
   $("#acontent").empty();
   $("#aimage").empty();
     $.post("<%=basePath%>/console/article/detailed.action", {gid:gid},
			   function(data){
			   
			   $("#A_PANEL").show();
			   $("#auther_icon").attr("src","<%=basePath%>/files/user-icon/"+data.account);
	           var json = eval('(' + data.content + ')');
	           var jsonImage = eval('(' + data.thumbnail + ')');
               $("#acontent").text(json.content);
                 
                if(data.type == 1)
                {
                    $("#aimage").append("<a href='"+json.link.link+"' width='95%' style='display: block;background:#E8E8E8;padding:20px;text-decoration:none;' target='_blank'>"+json.link.title+"</a>");
                    return;
                }
                
                
                if(data.type == 2)
                {
                	var width = $("#aimage").width();
                	var height = width * 2 / 3;
                    $("#aimage").append("<video src='<%=basePath%>/files/other-files/"+json.video.video+"' controls='controls' width='"+width+"' height='"+height+"'></video>");
                    return;
                }
                if(jsonImage == undefined){
                	return;
                }
                for(var i = 0;i<jsonImage.length;i++)
                {
                    if(jsonImage.length > 2)
                    {
                      $("#aimage").append("<img  onerror='onImageError(this)' src='<%=basePath%>/files/other-files/"+jsonImage[i].image+"'/ width='33%' style='padding-right: 0.3%;padding-top: 1px;'>");
                    }
                  
                    if(jsonImage.length == 2)
                    {
                      $("#aimage").append("<img  onerror='onImageError(this)' src='<%=basePath%>/files/other-files/"+jsonImage[i].image+"'/ width='50%' style='padding-right: 0.3%;padding-top: 1px;'>");
                    }
                    
                    if(jsonImage.length == 1)
                    {
                      $("#aimage").append("<img  onerror='onImageError(this)' src='<%=basePath%>/files/other-files/"+jsonImage[i].image+"'/ width='100%'>");
                    }
                }
                
		 });
	    
	     
    

}

  function onUserIconError(obj)
  {
		 obj.src="<%=basePath%>/resource/img/icon_head_default.png";
  }
  
  function onImageError(obj) {
		$(obj).removeAttr("src");
  }
</script>
</head>
<body class="web-app ui-selectable">


	<%@include file="../header.jsp"%>

	<%@include file="../nav.jsp"%>

	<div id="mainWrapper">
		<div class="lay-main-toolbar"></div>

		<div style="float: left; width: 70%;">
			<form action="<%=basePath%>/console/article/list.action"
				method="post" id="searchForm" style="padding: 0px;">
				<input type="hidden" name="currentPage" id="currentPage" value="1" />
				<table width="100%" class="utable">

					<thead>
						<tr class="tableHeader">

							<th width="20%">ID</th>
							<th width="20%">发布者</th>
							<th width="20%">类型</th>
							<th width="20%">发布时间</th>
							<th width="20%">操作</th>
						</tr>
						<tr>

							<td></td>
							<td style="padding: 2px;"><input name="account" type="text"
								value="${article.account }" class="form-control"
								style="width: 90%; display: inline;" /></td>
							<td><select name="type" id="Mtype"
								style="width: 90%; display: inline;" class="form-control">
									<option />
									<option value="0"
										<c:if test="${article.type eq '0'}"> selected="selected"</c:if>>图文</option>
									<option value="1"
										<c:if test="${article.type eq '1'}"> selected="selected"</c:if>>连接</option>
									<option value="2"
										<c:if test="${article.type eq '2'}"> selected="selected"</c:if>>视频</option>

							</select></td>
							<td></td>
							<td>
								<button type="submit" class="btn btn-primary btn-sm">
									<span class="glyphicon glyphicon-search"></span> 查询
								</button>
							</td>
						</tr>

					</thead>
					<tbody id="checkPlanList">

						<c:forEach var="article" items="${page.dataList}">
							<tr id="${article.gid}" style="height: 50px;">

								<td>${article.gid }</td>
								<td>${article.account }</td>

								<td><c:if test="${article.type eq '0'}">
										          图文
										    </c:if> <c:if test="${article.type eq '1'}">
										        连接
										    </c:if> <c:if test="${article.type eq '2'}">
										        视频
										    </c:if></td>

								<td><table:datetime timestamp="${article.timestamp }" /></td>

								<td>

									<div class="btn-group btn-group-xs">
										<button type="button" class="btn btn-primary"
											style="padding: 5px;"
											onclick="showDetailed('${article.gid}')" />
										<span class="glyphicon glyphicon-eye-open"></span>查看
									</div>

								</td>
							</tr>
						</c:forEach>

					</tbody>
					<tfoot>
						<tr>
							<td colspan="5"><table:page page="${page}" /></td>
						</tr>

					</tfoot>
				</table>
			</form>

		</div>

		<div id="A_PANEL"
			style="float: right; width: 29%; display: none; min-height: 800px; border: solid 1px #B5BBBF; margin-right: 2px; background: #FFFFFF;">
			<img src="<%=basePath%>/resource/img/circle_banner.jpg" width="100%"
				style="margin-top: -30px;" /> <img id="auther_icon"
				onerror='onUserIconError(this)'
				src="<%=basePath%>/resource/img/icon_head_default.png"
				class="gdialog" width="80px;"
				style="right: 40px; margin-top: -40px; position: absolute; background-color: white; padding: 2px;" />
			<div id="acontent" style="padding: 10px; margin-top: 60px;"></div>

			<div id="aimage" style="padding: 10px; margin-top: 10px,"></div>
		</div>
	</div>

	<script>
		$('#articleMenu').addClass('current');
	</script>
</body>
</html>
