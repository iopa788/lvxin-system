<%
	String navBasePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>

<style>
 .btn-purple:hover,.btn-purple:active,.btn-purple:focus{
    background-color: #763DDB;
    background-image: none;
    border-color:#421198;
    color: white;
 }
 
  .btn-purple{
    background-color: #5F28C0;
    background-image: none;
    color: white;
    border-color:#421198;
 }
</style>

<div id="_main_nav" class="ui-vnav">
	<ul class="ui-nav-inner">
	
	 <li style="height: 50px;text-align: center;margin-top: 10px;">
			 
			<a type="button" target="_blank" onclick="doShowDialog('webClientDialog')" class="btn btn-danger" >
				<span class="glyphicon glyphicon-globe"></span> WEB版本
			</a>		 
		</li>
		
           <li style="height: 50px;text-align: center;">
			 
			<a type="button" target="_blank" href="http://staticres.oss-cn-hangzhou.aliyuncs.com/lvxin-pro.apk" class="btn btn-success" >
					<span class="glyphicon glyphicon-download-alt"></span> 本地下载
			</a>		 
		</li>
		
		 
		 <li style="height: 50px;text-align: center;">
			 <button type="button" class="btn btn-primary" onclick="doShowDialog('scanDownloadDialog')">
					<span class="glyphicon glyphicon-qrcode"></span> 扫码下载
			 </button>
							 
		</li>
		<li style="border-bottom: 1px solid #D1D6DA;"></li>
			<li  class="ui-item" id="sessionMenu">
				<a href="<%=navBasePath %>/console/session/list.action">
				    <span class="glyphicon glyphicon-phone" style="top:2px;font-size: 14px;right:5px;"></span>
					<span class="ui-text">在线用户</span>
				</a>
			</li>
			<li  class="ui-item" id="broadcastMenu">
				<a href="<%=navBasePath%>/console/broadcast.action">
					<span class="glyphicon glyphicon-bullhorn" style="top:2px;font-size: 14px;right:5px;"></span>
					<span class="ui-text">发送广播</span>
				</a>
			</li>
		    <li  class="ui-item" id="userMenu">
				<a href="<%=navBasePath %>/console/user/list.action">
				    <span class="glyphicon glyphicon-user" style="font-size: 14px;right:5px;"></span>
					<span class="ui-text">用户管理</span>
				</a>
			</li>
			 <li  class="ui-item" id="orgMenu">
			     <a href="<%=navBasePath %>/console/organization/list.action">
			        <span class="glyphicon glyphicon-th" style="font-size: 14px;right:5px;"></span>
					<span class="ui-text">组织管理</span>
				 </a>
			</li>
			
			<li  class="ui-item" id="groupMenu">
				<a href="<%=navBasePath %>/console/group/list.action">
					<span class="glyphicon glyphicon-th-large" style="font-size: 14px;right:5px;"></span>
					<span class="ui-text">群组管理</span>
				 </a>
			</li>
			
		    <li  class="ui-item" id="publicAccountMenu">
				<a href="<%=navBasePath %>/console/publicAccount/list.action">
					<span class="glyphicon glyphicon-align-justify" style="font-size: 14px;right:5px;"></span>
					<span class="ui-text">公众账号</span>
				 </a>
			</li>
			<li  class="ui-item" id="articleMenu">
				<a href="<%=navBasePath %>/console/article/list.action">
					<span class="glyphicon glyphicon-book" style="font-size: 14px;right:5px;"></span>
					<span class="ui-text">文章管理</span>
				 </a>
			</li>
			<li  class="ui-item" id="messageMenu">
				<a href="<%=navBasePath %>/console/message/list.action?status=">
					<span class="glyphicon glyphicon-comment" style="font-size: 14px;right:5px;"></span>
					<span class="ui-text">消息查询</span>
				</a>
			</li>
			 
			
			<li  class="ui-item" id="configMenu">
				<a href="<%=navBasePath %>/console/config/list.action">
					<span class="glyphicon glyphicon-cog" style="font-size: 14px;right:5px;"></span>
					<span class="ui-text">系统配置</span>
			     </a>
			</li>
		 
	</ul>
	 
</div>
<div class="modal fade" id="scanDownloadDialog" tabindex="-1" role="dialog" >
	<div class="modal-dialog"  aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"aria-label="Close"> <span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabeeele2">
					扫描二维码下载APK
				</h4>
			</div>
			<div class="modal-body thumbnail" style="text-align: center;">
			    <img src = "<%=navBasePath%>/resource/img/lvxin_qrcode.png"/>
		    </div>
		 </div>
	</div>
</div>
<div class="modal fade  ui-draggable" id="webClientDialog" tabindex="-1" role="dialog"  data-backdrop="static" >
	<div class="modal-dialog" style="width: 960px;">
		<div class="modal-content"  >
			<div class="modal-header ui-draggable-handle" style="cursor: move;" >
				<h4 class="modal-title" id="myModalLabeeele2">
					WEB侣信体验
				</h4>
			</div>
			<div class="modal-body" style="text-align: center;padding: 0px;">
			    <iframe id="webIframe" style="border: 0px;border-bottom-left-radius: 4px;border-bottom-right-radius: 4px;" src="<%=navBasePath %>/webclient/index.jsp" width="958px" height="720px"></iframe>
		    </div>
		 </div>
	</div>
</div>

<script type="text/javascript">
$(".ui-draggable").draggable({   
   handle: ".ui-draggable-handle",   
   refreshPositions: false  
}); 
</script>