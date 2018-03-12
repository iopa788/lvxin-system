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
	    <script type="text/javascript">
	  
	    function doPushMessage() 
	     {
	        var content = $('#content').val();
		    if($.trim(content)=='')
		    {
		       return;
		    }
		    if($.trim(content).length > 500)
		    {
		       showETip("内容长度不能大于500个字");
		    }
		    var url = "<%=basePath%>/console/message/broadcastOnline.action";
		    if($("#bar_all").hasClass('active')){
		        url = "<%=basePath%>/console/message/broadcast.action";
		    }
		    showProcess('正在发送，请稍候......');
		    $.post(url, {content:content,action:2,sender:'system',format:'0'},
			   function(data){
			      hideProcess();
			      showSTip("发送成功");
			      $('#content').val("");
		     });
	     }
	    </script>
		</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper" style="text-align: center;">
		<div class="lay-main-toolbar">
		   <div class="btn-group" data-toggle="buttons">
					  <label id="bar_online" style="padding: 10px 20px;outline:none;" class="btn btn-primary bar-button active" >
					    <input type="radio" name="options" id="option1" autocomplete="off" checked/>在线用户 
					  </label>
					  <label id="bar_all" style="padding: 10px 20px;outline:none;" class="btn btn-primary bar-button" >
					    <input type="radio" name="options" id="option2" autocomplete="off"/>全部用户
					  </label>
				</div>
        </div>
		 
				

					<textarea rows="5" class="form-control" cols="30" placeholder="消息内容500字以内"
							style="width: 600px; height: 300px; padding: 5px;resize: none;margin:20px auto;"
							id="content"></textarea>
					 
 
			 
			<button type="button" class="btn btn-success btn-lg" style="width: 260px;margin:50px; auto;" onclick="doPushMessage()">
						<span class="glyphicon glyphicon-send" style="top:2px;"></span> 发送
			</button>		 
		</div>
		<script>
		       $('#broadcastMenu').addClass('current');
		</script>
	</body>
</html>
