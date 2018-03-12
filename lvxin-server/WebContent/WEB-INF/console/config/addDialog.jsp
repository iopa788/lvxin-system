
<%@ page language="java" pageEncoding="utf-8"%>
<%
	String addBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<script type="text/javascript">
     function doAdd()
	{
		    var domain = $('#Adomain').val();
		    var key = $('#Akey').val();
		    var value = $('#Avalue').val();
		    if($.trim(key)=='' || $.trim(value)=='' || $.trim(domain)=='')
		    {
		       return;
		    }
		    
		    showProcess('正在保存，请稍候......');
		    $.post("<%=addBasePath%>/console/config/save.action", {domain:domain,key:key,value:value},
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

<div class="modal fade" id="AddDialog" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel2">
	<div class="modal-dialog" style="width: 400px;" aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel2">
					添加配置
				</h4>
			</div>
			<div class="modal-body">

				<form role="form">
					<div class="form-group">
						<label for="Aaccount" style="width: 35px;">
							<font color="red">*</font>域:
						</label>
						<input type="text" class="form-control" id="Adomain"
							maxlength="32" style="display: inline; width: 320px;" />
					</div>
					<div class="form-group">
						<label for="Aname" style="width: 35px;">
							<font color="red">*</font>键:
						</label>
						<input type="text" class="form-control" id="Akey" maxlength="32"
							style="display: inline; width: 320px;" />
					</div>
					<div class="form-group">
						<label for="Aname" style="width: 35px;">
							<font color="red">*</font>值:
						</label>
						<textarea rows="5" class="form-control" cols="30"
							style="display: inline; width: 320px; height: 200px;" id="Avalue"></textarea>
					</div>

				</form>
			</div>

			<div class="modal-footer"
				style="padding: 5px 10px; text-align: center;">
				<a type="button" class="btn btn-success btn-lg" onclick="doAdd()"
					style="width: 200px;"> 保 存</a>
			</div>
		</div>
	</div>
</div>
