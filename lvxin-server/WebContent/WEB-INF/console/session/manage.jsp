<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/tld/table.tld" prefix="table"%>
<%@ taglib uri="/WEB-INF/tld/function.tld" prefix="function"%>
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
	 
		  
		function showMessageDialog(account)
	    {
		  $('#messageDialog').modal('show')
		  $('#Saccount').val(account);
		}
		
		function doSendMessage()
		{
		    var message = $('#message').val();
		    var account = $('#Saccount').val();
		    if($.trim(message)=='')
		    {
		       return;
		    }
		    showProcess('正在发送，请稍候......');
		    $.post("<%=basePath%>/console/message/send.action", {content:message,action:2,sender:'system',receiver:account,format:'0'},
			   function(data){
			   
			      hideProcess();
			      showSTip("发送成功");
			      doHideDialog("messageDialog");
		     });
		}
		  
		  
		function onImageError(obj)
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
			</div>



			<div>
				<form action="<%=basePath%>/console/session/list.action" method="post"
					id="searchForm" style="padding: 0px;">
					<input type="hidden" name="currentPage" id="currentPage" value="1"/>
					<table style="width: 100%" class="utable">

						<thead>
							<tr class="tableHeader">
								<th width="5%">
									头像
								</th>
								<th width="9%">
									账号
								</th>
								<th width="7%">
									 连接ID
								</th>
								<th width="9%">
									终端
								</th>
								<th width="9%">
									应用版本
								</th>
								<th width="8%">
									终端型号
								</th>
								<th width="9%">
									终端版本
								</th>
								<th width="9%">
									在线时长(秒)
								</th>
								<th width="25%">
									位置
								</th>
								<th width="12%">
									操作
								</th>
							</tr>
                            <tr>
                                    <td>
									</td>
									<td>
										<input name="account" type="text" value="${cimsession.account }"
											class="form-control"
											style="margin: 3px 0px;height: 32px;" />
									</td>
									 <td>
									  <input name="nid" type="number" <c:if test="${cimsession.nid ne null}"> value="${cimsession.nid }"</c:if> 
											class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"
											style="margin: 3px 0px;height: 32px;" />
									</td>
									<td>
									<select name="channel" class="form-control" style="width: 100%;padding：6px;">
									 <option />
									 <option value="android" <c:if test="${cimsession.channel eq 'android'}"> selected="selected"</c:if>>Android</option>
									 <option value="ios" <c:if test="${cimsession.channel eq 'ios'}"> selected="selected"</c:if>>IOS</option>
									 <option value="browser" <c:if test="${cimsession.channel eq 'browser'}"> selected="selected"</c:if>>Browser</option>
									</select>
									</td>
									<td>
									     <input name="clientVersion" type="text" value="${cimsession.clientVersion }"
											class="form-control"
											style="margin: 3px 0px;height: 32px;" />
									</td>
									<td>
									      
									</td>
									
									<td>
									 <input name="systemVersion" type="text" value="${cimsession.systemVersion }"
											class="form-control"
											style="margin: 3px 0px;height: 32px;" />
									</td>
									<td>
									</td>
									<td>
									</td>
									<td >
										<button type="submit" class="btn btn-primary btn-sm">
											<span class="glyphicon glyphicon-search"></span> 查询
										</button>
									</td>
								</tr>
						</thead>
						<tbody>
                         <c:forEach var="cimsession" items="${page.dataList}">
							<tr style="height: 50px;">
							    <td>
									<img width="40px" height="40px" onerror='onImageError(this)'
										src="<%=basePath%>/files/user-icon/${cimsession.account }" />
								</td>
							    <td>
									${cimsession.account }
								</td>
								<td>
									${cimsession.nid }
								</td>
								<td>
									${cimsession.channel }
								</td>
								<td>
									${cimsession.clientVersion }
								</td>
								<td>
									${cimsession.deviceModel }
								</td>
                                <td>
									${cimsession.systemVersion }
								</td>
								<td>
								 ${function:timeAgo(cimsession.bindTime)}
								</td>
								<td>
								 ${cimsession.location }
								</td>
								<td>
									<div class="btn-group btn-group-xs">
										<button type="button" class="btn btn-primary" 
											style="padding: 5px;"
											onclick="showMessageDialog('${cimsession.account}')">
											<span class="glyphicon glyphicon-send" style="top:2px;"></span> 发送消息
										</button>

									</div>
								</td>
							</tr>
						</c:forEach>
						</tbody>
                        <tfoot>
							<tr>
									<td colspan="10">
										<table:page page="${page}"/>
									</td>
								</tr>
							
						</tfoot>
					</table>
				</form>

			</div>
		</div>

<div class="modal fade" id="messageDialog" tabindex="-1" role="dialog" >
		<div class="modal-dialog" style="width: 420px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                <h4 class="modal-title">发送消息</h4>
				</div>
				<div class="modal-body">
					<div class="form-groupBuy">
							<label for="Amobile">
								接收账号:
							</label>
							<input type="text" class="form-control" id="Saccount"
								name="account"
								style="width: 100%; font-size: 20px; font-weight: bold;height:40px;"
								disabled="disabled" />
						</div>
						<div class="form-groupBuy" style="margin-top: 20px;">
							<label for="exampleInputFile">
								消息内容:
							</label>
							<textarea rows="10" style="width: 100%; height: 200px;"
								id="message" name="message" class="form-control"></textarea>
						</div>
				</div>
				<div class="modal-footer" style="padding: 5px 10px; text-align: center;">
					<button type="button" class="btn btn-success btn-lg" style="width: 200px;" onclick="doSendMessage()">
						<span class="glyphicon glyphicon-send" style="top:2px;"></span> 发送
					</button>
				</div>
			</div>
		</div>
</div>

		<script>
		       $('#sessionMenu').addClass('current');
		       
		</script>
	</body>
</html>
