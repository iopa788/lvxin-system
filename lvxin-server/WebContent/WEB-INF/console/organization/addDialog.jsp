	<%@ page language="java" pageEncoding="utf-8"%>
	<%
	String addBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
    %>
	<script type="text/javascript">
    function doAdd()
	{
		    var name = $('#Aname').val();
		    var sort = $('#Asort').val();
		    var parentCode = $('#Apcode').val();
		    var code = $('#Acode').val();
		    if($.trim(name)=='' || $.trim(sort)==''|| $.trim(code)=='')
		    {
		       return;
		    }
		    showProcess('正在保存，请稍候......');
		    $.post("<%=addBasePath%>/console/organization/save.action", {code:$.trim(code),name:name,sort:sort,parentCode:$.trim(parentCode)},
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
			         showETip("上级组织编号不存在");
			      }
			      if(data.code == 101)
			      {
			         showETip("组织编号已经存在");
			      }
		     });
	}
		  
   
   </script>
   
   <div class="modal fade" id="AddDialog" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabele2">
	<div class="modal-dialog" style="width: 400px;" aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabele2">
					添加组织
				</h4>
			</div>
			<div class="modal-body">
	 
							<form role="form">
								<div class="form-group" style="margin-top:10px;">
									<label for="Aaccount"  style="width: 75px;">
										<font color="red">*</font>名称:
									</label>
									<input type="text" class="form-control" id="Aname"
										maxlength="32" style="display: inline; width: 280px;height: 40px;" />
									<input type="hidden"  id="Ecode" />
								</div>
								<div class="form-group" style="margin-top:25px;">
									<label for="Aname"  style="width: 75px;">
										<font color="red">*</font>组织编号:
									</label>
									<input type="text" class="form-control" id="Acode"
										maxlength="32" style="display: inline; width: 280px;height: 40px;" />
								</div>
								<div class="form-group" style="margin-top:25px;">
									<label for="Aname"  style="width: 75px;">
										上级编号:
									</label>
									<input type="text" class="form-control" id="Apcode" placeholder="顶级部门请不要填写"
										maxlength="32" style="display: inline; width: 280px;height: 40px;" />
								</div>
								<div class="form-group" style="margin-top:25px;">
									<label for="Aname"  style="width: 75px;">
										<font color="red">*</font>排序:
									</label>
									<input type="number" class="form-control" id="Asort" maxlength="2" style="display: inline; width: 280px;height: 40px;" />
								</div>
							</form>
						</div>
						<div class="panel-footer" style="padding:5px 10px;text-align: center;">
						     <a type="button" class="btn btn-success btn-lg" onclick="doAdd()"  style="width: 200px;"> 保 存</a>
						</div>
					</div>
					</div>
					</div>