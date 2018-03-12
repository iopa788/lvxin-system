
<%@ page language="java" pageEncoding="utf-8"%>
<%
	String versionBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<script type="text/javascript">
     function doAddVersion()
	{
		    var versionCode = $('#AversionCode').val();
		    var versionName = $('#AversionName').val();
		    var url = $('#AappUrl').val();
		    var description= $('#AversionDes').val();
		    if($.trim(versionCode)=='' || $.trim(versionName)=='' || $.trim(url)=='')
		    {
		       return;
		    }
		    
		    showProcess('正在保存，请稍候......');
		    $.post("<%=versionBasePath%>/console/config/saveVersion.action", {domain:'lvxin_android',versionCode:versionCode,versionName:versionName,url:url,description:description},
			   function(data){
			      if(data.code == 200)
			      {
			        showSTip("添加成功");
			        doHideDialog('AddDialog');
			        window.location.href=$('#searchForm').attr('action');
			      }
			      if(data.code == 401)
			      {
			         hideProcess();
			         showETip("参数已存在,[域,键]重复");
			      }
			      if(data.code == 403)
			      {
			         hideProcess();
			         showETip("参数不合法");
			      }
		     });
		}
		  
   </script>

<div class="modal fade" id="AddVersionDialog" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel2">
	<div class="modal-dialog" style="width: 480px;" aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel2">
					配置新版本
				</h4>
			</div>
			<div class="modal-body">

				<form role="form">
				<div class="input-group">
				  <span class="input-group-addon" style="padding: 6px 8px;"><font color="red">*</font>版本等级</span>
				  <input type="number" class="form-control" id="AversionCode" placeholder="App的版本号数字，如1000"
							maxlength="10" style="display: inline; width: 350px;" />
				</div>
				
				<div class="input-group" style="margin-top: 10px;">
				  <span class="input-group-addon" style="padding: 6px 8px;"><font color="red">*</font>版本名称</span>
				  <input type="text" class="form-control" id="AversionName" maxlength="20" placeholder="App的版本名称，如1.3.0"
							style="display: inline; width: 350px;" />
				</div>	 
			    <div class="input-group" style="margin-top: 10px;">
				  <span class="input-group-addon" style="padding: 6px 8px;"><font color="red">*</font>下载地址</span>
				  <input type="text" class="form-control" id="AappUrl" maxlength="320" placeholder="App下载地址，如http://oss.aliyun.com/lvxin.apk"
							style="display: inline; width: 350px;" />
				</div>	
				  
				 <div class="input-group" style="margin-top: 10px;">
				  <span class="input-group-addon" style="padding: 6px 8px;"><font color="red">*</font>版本说明</span>
				  <textarea rows="5" class="form-control" cols="30" placeholder="App版本描述信息"
							style="display: inline; width: 350px; height: 200px;" id="AversionDes"></textarea>
				</div>	  	 

				</form>
			</div>

			<div class="modal-footer"
				style="padding: 5px 10px; text-align: center;">
				<a type="button" class="btn btn-success btn-lg" onclick="doAddVersion()"
					style="width: 200px;">保 存</a>
			</div>
		</div>
	</div>
</div>
