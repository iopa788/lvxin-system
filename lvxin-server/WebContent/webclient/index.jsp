<%@ page language="java"   pageEncoding="utf-8"%>
<%
	String resPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<title>侣信网页版</title>
<link rel="stylesheet" href="<%=resPath%>/resource/layim/css/layui.css">
<script src="<%=resPath%>/resource/layim/layui.js"></script>
<script src="<%=resPath%>/resource/js/cim.web.sdk.js"></script>
<link rel="shortcut icon" href="<%=resPath%>/resource/img/favicon.ico"type="image/x-icon" />
<link rel="stylesheet" href="<%=resPath%>/resource/bootstrap-3.3.7-dist/css/bootstrap.min.css" />
<link rel="stylesheet" href="<%=resPath%>/resource/css/base-ui.css" />
<script  src="http://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
  crossorigin="anonymous"></script>
<script type="text/javascript" src="<%=resPath%>/resource/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=resPath%>/resource/js/framework.js"></script>
<script type="text/javascript" src="<%=resPath%>/resource/js/layim.config.js"></script>
<script type="text/javascript" src="<%=resPath%>/resource/js/cim.controler.js"></script>
<script>
$(document).ready(function(){
	$('#LoginDialog').fadeIn();
    $('#LoginDialog').addClass("in");
 });
 
//禁止鼠标右键菜单
document.oncontextmenu = function(e){
       return false;
}
</script>
</head>
<body>
 <div class="modal fade" id="LoginDialog" tabindex="-1" role="dialog"   data-backdrop="static">
 <div class="modal-dialog" style="width: 300px;margin: 30px auto;">
		<div class="modal-content" >
			<div class="modal-body" style="padding:0px;" >
            <div  style="height:150px;text-align: center; background: #5FA0D3; color: #ffffff; border: 0px; border-top-left-radius: 4px; border-top-right-radius: 4px;">
	        <img src="<%=resPath %>/resource/img/icon.png" style="height: 60px;width: 60px;margin-top:30px;"/>
	        <div style="margin-top: 20px; color: #ffffff;font-size: 16px;">使用用户帐号登录</div>
 		    </div>
	        	<div class="input-group" style="margin-top: 20px;margin-left:10px;margin-right:10px;margin-bottom:20px;">
	        	  <span class="input-group-addon"><span class="glyphicon glyphicon-user" aria-hidden="true"></span></span>
				  <input type="text" class="form-control" id="account" maxlength="32" placeholder="帐号"
					style="display: inline; width: 100%; height: 50px;" />
				</div>	 
			    <div class="input-group" style="margin-top: 20px;margin-left:10px;margin-right:10px;margin-bottom:20px;">
	        	  <span class="input-group-addon"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span></span>
				  <input type="password" class="form-control" id="password" maxlength="32" placeholder="密码"
					style="display: inline; width: 100%; height: 50px;" />
				</div>	 
		    </div>
			<div class="modal-footer" style="text-align: center;">
				<a type="button" class="btn btn-success btn-lg" onclick="doLogin('<%=resPath%>')"
					style="width: 250px;">登录</a>
			</div>
      </div>
      </div>
</div>

<div id="global_mask" style="display: none; position: absolute; top: 0px; left: 0px; z-index: 9999; background-color: rgb(20, 20, 20); opacity: 0.5; width: 100%; height: 100%; overflow: hidden; background-position: initial initial; background-repeat: initial initial;"></div>

</body>
</html>
