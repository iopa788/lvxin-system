<%@ page language="java" pageEncoding="utf-8"%>
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

<%@include file="./resources.jsp"%>
<style type="text/css">

.login-wrapper {
    background: #e1e4e7 url(<%=basePath%>/resource/img/pattern.png) repeat;
    background: url(<%=basePath%>/resource/img/pattern.png) repeat,linear-gradient(#e1e4e7,#f3f4f5);
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 100;
}

</style>
<script>
$(function () {
  $('[data-toggle="popover"]').popover();
});
function doLogin()
{
	    var account = $('#account').val();
	    var password = $('#password').val();
	    if($.trim(account)=='' || $.trim(password)=='')
	    {
	       return;
	    }
	    
	    showProcess('正在登录请稍候......');
	    $.post("<%=basePath%>/system/login.do", {account:account,password:password},
		   function(data){
		      hideProcess();
		      if(data == 403)
		      {
		         showETip("账号或者密码错误");
		         return ;
		      }
		      doHideDialog('LoginDialog');
		      window.location.href="<%=basePath%>/console/index.action";
	     });
	}
</script>
</head>
<body class="login-wrapper">

	<div class="modal-dialog" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header" style="text-align: center;color:#ffffff;border:0px;">
			</div>
			<div class="modal-body" style="padding:25px;">

            <div  style="text-align: center; height: 150px;">
	        <img src="<%=basePath %>/resource/img/icon.png" style="height: 80px;width: 80px;"/>
	        <div style="margin-top: 20px; color: #979797;font-size: 16px;">欢迎使用侣信专业版管理系统</div>
	        <div style="margin-top: 10px; color: #979797;">请关注<a target="_blank" href="https://gitee.com/farsunset/lvxin-pro">项目主页</a>，及时获得更新</div>
		</div>
		   
	        	<div class="input-group" style="margin-top: 20px;">
	        	  <span class="input-group-addon"><span class="glyphicon glyphicon-user" aria-hidden="true"></span></span>
				  <input type="text" class="form-control" id="account" maxlength="32" placeholder="管理员帐号"
					style="display: inline; width: 310px; height: 50px;" />
				</div>	 
				
				<div class="input-group" style="margin-top: 20px;">
				  <span class="input-group-addon"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span></span>
				  <input type="password" class="form-control" id="password" placeholder="密码"
					maxlength="32" style="display: inline; width: 310px; height: 50px;" />
				</div>	 
			 
			    <div class="input-group" style="margin-top: 20px;color: #979797;width: 100%;text-align: right;">
				    <span tabindex="0" role="button"   data-trigger="focus"    data-toggle="popover" title="提示" data-placement="left" data-content="默认帐号和密码是 system ,system。如果密码被更改，请联系管理员在数据库(t_lvxin_manager表)中重置密码，密码是MD5加密后的密文.">忘记密码<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span></span>
				</div>	
			 
		</div>
		<div class="modal-footer" style="text-align: center;">
			<a type="button" class="btn btn-success btn-lg" onclick="doLogin()"
				style="width: 320px;">登录</a>
		</div>
</div>
</div>
<div id="global_mask" style="display: none; position: absolute; top: 0px; left: 0px; z-index: 9999; background-color: rgb(20, 20, 20); opacity: 0.5; width: 100%; height: 100%; overflow: hidden; background-position: initial initial; background-repeat: initial initial;"></div>

<script>
doShowDialog('LoginDialog');
</script>
</body>


</html>
