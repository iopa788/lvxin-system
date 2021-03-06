
<%@ page language="java" pageEncoding="utf-8"%>
<%
	String editBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<script type="text/javascript">
    var account;
    function doSaveBasic()
	{
		    var name = $('#Ename').val();
		    var description = $('#Edescription').val();
		    var greet = $('#Egreet').val();
		    var link = $('#Elink').val();
		    if($.trim(description)=='' || $.trim(name)=='')
		    {
		       return;
		    }
		    
		    showProcess('正在保存，请稍候......');
		    $.post("<%=editBasePath%>/console/publicAccount/modifyBasic.action", {account:account,name:name,description:description,greet:greet,link:link},
			   function(data){
			      hideProcess();
			      showSTip("保存成功");
			      
		     });
	}
	
	function doSaveApi()
	{
		    var apiUrl = $('#EapiUrl').val();
		    if($.trim(apiUrl)=='')
		    {
		       return;
		    }
		    
		    showProcess('正在保存，请稍候......');
		    $.post("<%=editBasePath%>/console/publicAccount/modifyApi.action", {account:account,apiUrl:apiUrl},
			   function(data){
			      hideProcess();
			      showSTip("保存成功");
			      
		     });
	}
		  
    function showConfigureDialog(paccount)
    {
        account = paccount;
        $.post("<%=editBasePath%>/console/publicAccount/detailed.action", {account:paccount},
			   function(data){
			     $('#Ename').val(data.data.name);
			     $('#Edescription').val(data.data.description);
			     $('#Elink').val(data.data.link);
			     $('#Egreet').val(data.data.greet);
			     $('#EapiUrl').val(data.data.apiUrl);
			      
		});
		onActionBarClick(0);
		$(".bar-button").removeClass("active");
		$("#bar_basic").addClass("active");
        doShowDialog('configureDialog');
        $("#logoImage").attr("src","<%=editBasePath%>/files/pub-icon/"+account);
    }
    
    function onActionBarClick(index)
    {
    
        $('.config-view').hide();
        $("#configureDialog").find('.modal-footer').hide();
        if(index==0){
          
          $('.baseinfo').show();
          $('#doSaveBasicButtonView').show();
          
        }
        if(index==1){
          
          $(".apiinfo").show();
          $('#doSaveAPIURLButtonView').show();
          
        }
        if(index==2){
          
          $(".logoview").show();
          $('#doSaveLogoButtonView').show();
          
        }
        
    }
    
    function doSetLogo(){
       var filename = $("#logoFile").val();
       $("#Gaccount").val(account);
       if(!isImageFile(filename)){
          showETip("请上传JPG或者PNG格式的图片");
          return;
       }
       showProcess('正在保存，请稍候......');
       $("#logoForm").submit();
    }
    
    function  onFileUploadCallbak(code){
      hideProcess();
      if(code == 200){
        showSTip("保存成功");
        $("#logoImage").attr("src","<%=editBasePath%>/files/pub-icon/"+account+"?r="+Math.random());
      }
      if(code == 403){
        showETip("您上传的不是图片文件,请重新上传");
      }
      if(code == 413){
        showETip("文件太大，超过了200KB");
      }
      if(code == 500){
        showETip("保存失败，请稍后再试");
      }
    }
    
    function onPubIconError(obj)
	{
		$(obj).removeAttr("src");
	}
	
	$(document).ready(function(){ 
         $("#logoFile").change(function (){
           $("#filePath").val($(this).val());
         });
    });
	
   </script>



<div class="modal fade" id="configureDialog" tabindex="-1" role="dialog"
	aria-labelledby="aeafefeafa">
	<div class="modal-dialog" style="width: 640px;" aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="aeafefeafa">
					修改配置
				</h4>
			</div>
			<div class="modal-body" style="text-align: center;">

                <div class="page-header" style= "margin: 0px;">
				    <div class="btn-group" data-toggle="buttons">
					  <label id="bar_basic" onclick="onActionBarClick(0)" style="padding: 10px 20px;outline:none;" class="btn btn-primary bar-button active" >
					    <input type="radio" name="options" id="option1" autocomplete="off" checked/>基本信息
					  </label>
					  <label id="bar_api" onclick="onActionBarClick(1)" style="padding: 10px 20px;outline:none;" class="btn btn-primary bar-button" >
					    <input type="radio" name="options" id="option2" autocomplete="off"/>接口配置
					  </label>
					  <label id="bar_api" onclick="onActionBarClick(2)" style="padding: 10px 20px;outline:none;" class="btn btn-primary bar-button" >
					    <input type="radio" name="options" id="option2" autocomplete="off"/>LOGO配置
					  </label>
				    </div>
				</div>
				<div style="text-align: center; margin: 50px auto;" class="config-view baseinfo">

                    <div class="input-group" style="margin: 0 auto;">
						<span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>名称</span>
						<input type="text" name="code" class="form-control"  id="Ename" maxlength="16" style="display: inline; width: 400px;height: 40px;" />
					</div>
					
					<div class="input-group"  style="margin: 20px auto;">
						<span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>简介</span>
                        <textarea rows="5" class="form-control" cols="30" style="display: inline; width: 400px; height: 150px; padding: 5px;"
							id="Edescription">
						</textarea>					
					</div>
					
					<div class="input-group"   style="margin: 20px auto;">
						<span class="input-group-addon" style="padding:0px;width: 60px;">欢迎语</span>
                        <textarea rows="5" class="form-control" cols="30" style="display: inline; width: 400px; height: 80px; padding: 5px;"
							id="Egreet">
						</textarea>					
					</div>
					
					<div class="input-group"   style="margin: 20px auto;">
						<span class="input-group-addon" style="padding:0px;width: 60px;">主页</span>
						<input type="text" name="code" class="form-control"  id="Elink" maxlength="200" style="display: inline; width: 400px;height: 40px;" />
					</div>

				</div>
				<div style="text-align: center; margin: 120px auto; display: none;"
					class="config-view apiinfo">

					<div class="form-group">
						<label for="Aname">
							接口地址:
						</label>
						<input type="text" class="form-control" id="EapiUrl"
							maxlength="200" style="display: inline; width: 500px;height:40px;" />
					</div>
					<div class="alert alert-info" role="alert">
						当用户点击事件菜单或者发送文字内容时，会post事件信息到这个url，并等待返回规定格式的消息给用户
					</div>
				</div>
				
				
				<div style="text-align: center; margin: 50px auto; display: none;"
					class="config-view logoview">
                    <form id="logoForm" enctype="multipart/form-data" action="<%=editBasePath%>/console/publicAccount/setLogo.action" method="post" target="logoIframe">
                   	<input name="account" type="hidden" id="Gaccount" />

                    <h4>Logo</h4>
					 <a href="#" class="thumbnail" style="width:120px;height:120px;margin: 0 auto;">
					       <img onerror='onPubIconError(this)' id="logoImage" style="width:100%;height:100%;background:#eeeeee"/>
					 </a>
					 
					<div style="margin: 50px auto;">
					    <input type="file" name = "file" id="logoFile" style="display:none;" />
						<input class="form-control" id="filePath" disabled="disabled" type="text" style="height:40px;width:400px;display:inline;">  
						<button type="button" class="btn btn-primary"  style="border-top-left-radius: 0; border-bottom-left-radius: 0;padding: 5px;height: 40px;margin-top: -2px;margin-left: -82px;" onclick="$('input[id=logoFile]').click();">
							<span class="glyphicon glyphicon-picture" ></span> 选择图片
						</button>
					</div>
					<div class="alert alert-info" role="alert">
						建议选择尺寸为128*128的图片，200KB以内
					</div> 
					</form>
					<iframe style="display: none;" id="logoIframe" name ="logoIframe"></iframe>
				</div>
				
			</div>
			 
			<div class="modal-footer"
				style="padding: 5px 10px; text-align: center; position: absolute; bottom: 0px; width: 100%;"
				id="doSaveAPIURLButtonView">
				<a type="button" class="btn btn-success btn-lg"
					onclick="doSaveApi()" style="width: 200px;"> 保 存</a>
			</div>


			<div class="modal-footer"
				style="padding: 5px 10px; text-align: center; display: none;"
				id="doSaveBasicButtonView">
				<a type="button" class="btn btn-success btn-lg"
					onclick="doSaveBasic()" style="width: 200px;">保 存</a>
			</div>
			
			<div class="modal-footer"
				style="padding: 5px 10px; text-align: center; display: none;"
				id="doSaveLogoButtonView">
				<a type="button" class="btn btn-success btn-lg"
					onclick="doSetLogo()" style="width: 200px;">保 存</a>
			</div>
			
		</div>
	</div>
</div>