	<%@ page language="java" pageEncoding="utf-8"%>
	<%
	String editBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
    %>
	<script type="text/javascript">
    function doModify()
	{
		    var name = $('#Mname').val();
		    var account = $('#Maccount').val();
		    var telephone = $('#Mtelephone').val();
		    var email = $('#Memail').val();
		    var code = $('#Mcode').val();
		    var gender = $('#Mgender').val();
		    if($.trim(name)=='' || $.trim(account)=='')
		    {
		       return;
		    }
		    showProcess('正在保存，请稍候......');
		    $.post("<%=editBasePath%>/console/user/update.action", {gender:gender,orgCode:$.trim(code),name:name,account:account,telephone:telephone,email:email},
			   function(data){
			      hideProcess();
			      if(data.code == 200)
			      {
			         showSTip("保存成功");
			         doHideDialog('EditDialog');
			         window.location.href=$('#searchForm').attr('action');
			      }
			      
			      if(data.code == 404)
			      {
			         showETip("组织编号不存在");
			      }
		     });
	}
		  
    function showEditDialog(model)
    {
        $('#Mcode').val(model.orgCode);
        $('#Maccount').val(model.account);
        $('#Memail').val(model.email);
        $('#Mname').val(model.name);
        $('#Mtelephone').val(model.telephone);
        $('#Mgender').val(model.gender);
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
					修改用户
				</h4>
			</div>
			<div class="modal-body">
	 
							<form role="form">
							    <div class="form-group" style="margin-top:10px;">
									<label for="Aaccount"  style="width: 75px;">
										<font color="red">*</font>帐号:
									</label>
									<input type="text" class="form-control" id="Maccount" disabled="disabled"
										maxlength="32" style="display: inline; width: 280px;height:40px;" />
								</div>
								<div class="form-group" style="margin-bottom:20px;">
									<label for="Aaccount"  style="width: 75px;">
										<font color="red">*</font>名称:
									</label>
									<input type="text" class="form-control" id="Mname"
										maxlength="32" style="display: inline; width: 280px;height:40px;" />
								</div>
								<div class="form-group" style="margin-bottom:20px;">
									<label for="Aaccount"  style="width: 75px;">
										<font color="red">*</font>性别:
									</label>
									<select name="gender" id="Mgender"  class="form-control"  style="display: inline;width: 280px;height:40px;">
									 <option value="1" >男</option>
									 <option value="0" >女</option>
									</select>
								</div>
								<div class="form-group" style="margin-bottom:20px;">
									<label for="Aname"  style="width: 75px;">
										 组织编号:
									</label>
									<input type="text" class="form-control" id="Mcode"
										maxlength="32" style="display: inline; width: 280px;height:40px;" />
								</div>
								<div class="form-group" style="margin-bottom:20px;">
									<label for="Aname"  style="width: 75px;">
										 电话号码:
									</label>
									<input type="text" class="form-control" id="Mtelephone"
										maxlength="12" style="display: inline; width: 280px;height:40px;" />
								</div>
								<div class="form-group" style="margin-bottom:20px;">
									<label for="Aname"  style="width: 75px;">
										邮箱:
									</label>
									<input type="text" class="form-control" id="Memail" maxlength="64" style="display: inline; width: 280px;height:40px;" />
								</div>
							</form>
						</div>
						<div class="panel-footer" style="padding:5px 10px;text-align: center;">
						     <a type="button" class="btn btn-success btn-lg" onclick="doModify()"  style="width: 200px;"> 保 存</a>
						</div>
					</div>
					</div>
					</div>