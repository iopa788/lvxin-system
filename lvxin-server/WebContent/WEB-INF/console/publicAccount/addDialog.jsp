
<%@ page language="java" pageEncoding="utf-8"%>
<%
	String addBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<script type="text/javascript">
     function doAdd()
	{
		    var account = $('#Aaccount').val();
		    var name = $('#Aname').val();
		    var description = $('#Adescription').val();
		    if($.trim(account)=='' || $.trim(name)=='' || $.trim(description)=='')
		    {
		       return;
		    }
		    
		    showProcess('正在保存，请稍候......');
		    $.post("<%=addBasePath%>/console/publicAccount/save.action", {account:account,name:name,description:description},
			   function(data){
			      hideProcess();
			      if(data.code==400)
			      {
			          showETip("账号已经存在或已经被用户账号占用");
			          
			      }
			      if(data.code==200)
			      {
			        showSTip("添加成功");
			        doHideDialog('AddDialog');
			        window.location.href=$('#searchForm').attr('action');
			      }
			      
		     });
		}
		  
   </script>


<div class="modal fade" id="AddDialog" tabindex="-1" role="dialog"
	aria-labelledby="fafafeafaffesf">
	<div class="modal-dialog" style="width: 450px;" aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="fafafeafaffesf">
					添加公众账号
				</h4>
			</div>
			<div class="modal-body" style="text-align: center;">

				<form role="form">
					<div class="form-group">
						<label for="Aaccount" style="width: 60px;">
							<font color="red">*</font>账号:
						</label>
						<input type="text" class="form-control" id="Aaccount"
							maxlength="32" style="display: inline; width: 320px;" />
					</div>
					<div class="form-group">
						<label for="Aname" style="width: 60px;">
							<font color="red">*</font>名称:
						</label>
						<input type="text" class="form-control" id="Aname" maxlength="32"
							style="display: inline; width: 320px;" />
					</div>
					<div class="form-group">
						<label for="Aname" style="width: 60px;">
							<font color="red">*</font>说明:
						</label>
						<textarea class="form-control" rows="5" cols="30"
							style="display: inline; width: 320px; height: 200px; padding: 5px;"
							id="Adescription"></textarea>
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