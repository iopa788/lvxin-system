    <%@ page language="java" pageEncoding="UTF-8" import="com.farsunset.lvxin.model.Manager"%>

<%
	String headerBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
	Manager admin = (Manager)session.getAttribute("manager");
%>
<script type="text/javascript">
   
    function doLogin()
	{
		    var account = $('#account').val();
		    var password = $('#password').val();
		    if($.trim(account)=='' || $.trim(password)=='')
		    {
		       return;
		    }
		    
		    showProcess('正在登录请稍候......');
		    $.post("<%=headerBasePath%>/system/login.do", {account:account,password:password},
			   function(data){
			      hideProcess();
			      if(data == 403)
			      {
			         showETip("账号或者密码错误");
			         return ;
			      }
			      doHideDialog('LoginDialog');
			      $("#loginButton").text("系统管理员");
			      $("#loginButton").removeAttr("onclick");
		     });
		}
	function doModifyAdminPassrod()
	{
		    var curPasswrod = $('#curPasswrod').val();
		    var newPasswrod = $('#newPasswrod').val();
		    var cfmPasswrod = $('#cfmPasswrod').val();
		    if($.trim(cfmPasswrod)=='' || $.trim(newPasswrod)=='' || $.trim(curPasswrod)=='')
		    {
		       return;
		    }
		    if(cfmPasswrod!=newPasswrod)
		    {
		       showETip("确认密码不一致");
		       return;
		    }
		    showProcess('正在保存请稍候......');
		    $.post("<%=headerBasePath%>/system/manager/modifyPassword.action", {oldPassword:curPasswrod,newPassword:newPasswrod},
			   function(data){
			      hideProcess();
			      if(data == 403)
			      {
			         showETip("当前密码错误");
			         return ;
			      }
			      if(data == 404)
			      {
			         window.location.href="<%=headerBasePath%>/login.jsp";
			         return ;
			      }
			      if(data == 200)
			      {
			         showSTip("密码修改成功");
			         doHideDialog('ModifyPasswordDialog');
			      }
		     });
		}	  
</script>


<div id="_main_fixed_header" class="header-fixed">

	<!-- 头部 -->
	<div id="_main_header_banner" class="header">
		<div id="_main_header_cnt" style="margin-left: 220px;margin-right: 20px;">
			<div class="logo" style="left: -200px;padding-top: 10px;position: relative;">
			 <img src="<%=headerBasePath%>/resource/img/top_logo.png"/>
			</div>

			<div class="btn-group" style="float: right;margin-top: -50px;">
				 
				<a type="button" class="btn btn-danger" onclick="doShowDialog('ModifyPasswordDialog')" id="passwordButton"
					target="_blank"><%=admin.getName() %></a>
				 
				<a type="button" class="btn btn-primary"
					onclick="doShowDialog('aboutDialog')"><span class="glyphicon glyphicon-info-sign"></span> 关于</a>
			</div>
		</div>

	</div>

	<!--web的导航在左侧-->

</div>

<div class="modal fade" id="aboutDialog" tabindex="-1" role="dialog">
		<div class="modal-dialog" style="width: 420px;">
			<div class="modal-content">
				<div class="modal-header" >
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                <h4 class="modal-title">侣信管理系统</h4>
				</div>
				<div class="modal-body">
 
	    <div style="text-align: center;border:none;height: 150px;">
									<img src="<%=headerBasePath%>/resource/img/icon.png" style="margin-top: 35px;height: 80px;height: 80px;"/>
									<h4>v2.5.0</h4>
		</div>
		<ul class="list-group" style="margin-top: 20px;">
			 
			<li class="list-group-item" style="border-radius: 0px;">
				侣信专业版是面向中小企业和者各类团队组织内部交流使用工具。它可以在局域网内使用保证沟通的信息安全，并且它是完全免费的，而且可以及时获得更新。  
				<p/><p/>项目主页地址<br/><a  target="_blank" href="https://gitee.com/farsunset/lvxin-pro">https://gitee.com/farsunset/lvxin-pro</a>
			</li>
			 
			<li class="list-group-item" style="border-radius: 0px;">
				作者:远方夕阳
			</li>
			<li class="list-group-item" style="border-radius: 0px;">
				Q Q:3979434
			</li>
			<li class="list-group-item" style="border-radius: 0px;">
				微信:farbluesky
			</li>
		</ul>
	</div>
</div>
</div>
</div>


<div class="modal fade" id="LoginDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" style="width: 320px;" aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header" >
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	            <h4 class="modal-title" id="myModalLabel">系统管理员登录</h4>
			</div>
			<div class="modal-body">
			    <div style="text-align: center;height: 100px;">
				    <img src="<%=headerBasePath %>/resource/img/icon.png" style="margin-top: 20px;height: 50px;width: 50px;"/>
				</div>
				<div class="form-group" style="margin-top: 20px;">
					<label style="width: 50px;">
						<font color="red">*</font>账号:
					</label>
					<input type="text" class="form-control" id="account" maxlength="15"style="display: inline; width: 200px; height: 50px;" />
				</div>
				<div class="form-group" style="margin-top: 20px;">
					<label style="width: 50px;">
						<font color="red">*</font>密码:
					</label>
					<input type="password" class="form-control" id="password" maxlength="32" style="display: inline; width: 200px; height: 50px;" />
				</div>
			</div>
			
			<div class="modal-footer" style="text-align: center;">
				<a type="button" class="btn btn-success btn-lg" onclick="doLogin()" style="width: 200px;">登 录</a>
			</div>
		</div>
    </div>
</div>

<div class="modal fade" id="ModifyPasswordDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" style="width:360px;" aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header" >
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	            <h4 class="modal-title" id="myModalLabel">修改管理员密码</h4>
			</div>
			<div class="modal-body">
			    
				<div class="form-group" style="margin-top: 20px;">
					<label style="width: 70px;">
						<font color="red">*</font>当前密码:
					</label>
					<input type="password" class="form-control" id="curPasswrod" maxlength="15"style="display: inline; width: 220px; height: 40px;" />
				</div>
				<div class="form-group" style="margin-top: 20px;">
					<label style="width: 70px;">
						<font color="red">*</font>新密码:
					</label>
					<input type="password" class="form-control" id="newPasswrod" maxlength="32" style="display: inline; width: 220px; height: 40px;" />
				</div>
				<div class="form-group" style="margin-top: 20px;">
					<label style="width: 70px;">
						<font color="red">*</font>确认密码:
					</label>
					<input type="password" class="form-control" id="cfmPasswrod" maxlength="32" style="display: inline; width: 220px; height: 40px;" />
				</div>
			</div>
			
			<div class="modal-footer" style="text-align: center;">
				<a type="button" class="btn btn-success btn-lg" onclick="doModifyAdminPassrod()" style="width: 200px;">保 存</a>
			</div>
		</div>
    </div>
</div>


<div id="global_mask" style="display: none; position: absolute; top: 0px; left: 0px; z-index: 9999; background-color: rgb(20, 20, 20); opacity: 0.5; width: 100%; height: 100%; overflow: hidden; background-position: initial initial; background-repeat: initial initial;"></div>
