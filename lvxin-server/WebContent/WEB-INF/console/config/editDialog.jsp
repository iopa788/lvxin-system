
<%@ page language="java" pageEncoding="utf-8"%>
<%
	String editBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<script type="text/javascript">
    function doEdit()
	{
		    var domain = $('#Edomain').val();
		    var key = $('#Ekey').val();
		    var value = $('#Evalue').val();
		    if($.trim(key)=='' || $.trim(value)=='' || $.trim(domain)=='')
		    {
		       return;
		    }
		    
		    showProcess('正在保存，请稍候......');
		    $.post("<%=editBasePath%>/console/config/update.action", {gid:$('#Egid').val(),domain:domain,key:key,value:value},
			   function(data){
			   
			      if(data.code == 200)
			      {
			         showSTip("保存成功");
			         doHideDialog('EditDialog');
			         window.location.href=$('#searchForm').attr('action');
			      }
			      if(data.code == 401)
			      {
			         hideProcess();
			         showETip("参数已存在,[域,键]重复");
			      }
		     });
	}
		  
    function showEditDialog(config)
    {
        $('#Egid').val(config.gid);
        $('#Edomain').val(config.domain);
        $('#Ekey').val(config.key);
        $('#Evalue').val(config.value);
        doShowDialog('EditDialog');
    }
   </script>

<div class="modal fade" id="EditDialog" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabele2">
	<div class="modal-dialog" style="width: 400px;" aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabele2">
					修改配置
				</h4>
			</div>
			<div class="modal-body">


				<form role="form">
					<div class="form-group">
						<label for="Aaccount" style="width: 35px;">
							<font color="red">*</font>域:
						</label>
						<input type="text" class="form-control" id="Edomain"
							maxlength="32" style="display: inline; width: 320px;" />
						<input type="hidden" id="Egid" />
					</div>
					<div class="form-group">
						<label for="Aname" style="width: 35px;">
							<font color="red">*</font>键:
						</label>
						<input type="text" class="form-control" id="Ekey" maxlength="32"
							style="display: inline; width: 320px;" />
					</div>
					<div class="form-group">
						<label for="Aname" style="width: 35px;">
							<font color="red">*</font>值:
						</label>
						<textarea rows="5" cols="30" class="form-control"
							style="display: inline; width: 320px; height: 200px;" id="Evalue"></textarea>
					</div>
				</form>
			</div>
			<div class="modal-footer"
				style="padding: 5px 10px; text-align: center;">
				<a type="button" class="btn btn-success btn-lg" onclick="doEdit()"
					style="width: 200px;"> 保 存</a>
			</div>
		</div>
	</div>
</div>