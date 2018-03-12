
<%@ page language="java" pageEncoding="utf-8"%>
<%
	String pushBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<script type="text/javascript">
    var publicAccount;
    function doPushTextMessage() 
	{
	        var content = $('#Pcontent').val();
		    if($.trim(content)=='')
		    {
		       showETip("请填写文字内容");
		       return;
		    }
		    var data ={content:content};
		    showProcess('正在发送，请稍候......');
		    $.post("<%=pushBasePath%>/console/publicAccount/push.action", {content:JSON.stringify(data),receiver:publicAccount,format:0},
			   function(data){
			      hideProcess();
			      showSTip("发送成功");
			      $('#Pcontent').val("");
		     });
	}
	
	function doPushLinkMessage() 
	{
	        var title = $('#Ptitle').val();
	        var text = $('#Ptext').val();
	        var link = $('#Plink').val();
	        var image = $('#Pimage').val();
		    if($.trim(title)=='')
		    {
		       showETip("请填写标题");
		       return;
		    }
		    if($.trim(link)=='')
		    {
		       showETip("请填写网址连接");
		       return;
		    }
		    if($.trim(image)=='')
		    {
		       showETip("请填写图片地址");
		       return;
		    }
		    if($.trim(text)=='')
		    {
		       showETip("请填写文字说明");
		       return;
		    }
		    var data ={title:title,content:text,link:link,image:image};
		    showProcess('正在发送，请稍候......');
		    $.post("<%=pushBasePath%>/console/publicAccount/push.action", {content:JSON.stringify(data),receiver:publicAccount,format:5},
			   function(data){
			      hideProcess();
			      showSTip("发送成功");
			      $('#Ptitle').val("");
	              $('#Ptext').val("");
	              $('#Plink').val("");
	              $('#Pimage').val("");
			      
		     });
	}
		  
    function showPushDialog(paccount)
    {
        publicAccount = paccount;
        doShowDialog('PushDialog');;
    }
    
    function onPushActionBarClick(index )
    {
        if(index==0){
          $('#bar_msg_text').addClass('active');
          $('#bar_msg_link').removeClass('active');
          
          $('#textMsgView').show();
          $('#linkMsgView').hide();
          $('#doPushLinkMessageButton').hide();
          $('#doPushTextMessageButton').show();
        }
        if(index==1){
          $('#bar_msg_text').removeClass('active');
          $('#bar_msg_link').addClass('active');
          
          $('#linkMsgView').show();
          $('#textMsgView').hide();
          $('#doPushLinkMessageButton').show();
          $('#doPushTextMessageButton').hide();
        }
        
    }
   </script>


<div class="modal fade" id="PushDialog" tabindex="-1"
	role="dialog" data-backdrop="static">
	<div class="modal-dialog" style="width: 720px;" aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="afafafavaac">
					推送消息
				</h4>
			</div>
			<div class="modal-body" style="text-align: center;">
                <input type="hidden" id="Gaccount" />
                <div class="page-header" style= "margin: 0px;">
				   <div class="btn-group" data-toggle="buttons">
					  <label id="bar_msg_text" onclick="javascript:onPushActionBarClick(0);" style="padding: 10px 20px;outline:none;" class="btn btn-primary bar-button active" >
					    <input type="radio" name="options" id="option1" autocomplete="off" checked/>文字消息
					  </label>
					  <label id="bar_msg_link" onclick="javascript:onPushActionBarClick(1);" style="padding: 10px 20px;outline:none;" class="btn btn-primary bar-button" >
					    <input type="radio" name="options" id="option2" autocomplete="off"/>链接消息
					  </label>
					  <!-- 
					  <label id="bar_msg_article" onclick="javascript:onPushActionBarClick(2);" style="padding: 10px 20px;outline:none;" class="btn btn-primary bar-button" >
					    <input type="radio" name="options" id="option2" autocomplete="off"/>图文消息
					  </label>
					   -->
				    </div>
				</div>
				<div style="text-align: center; margin: 20px auto; height: 340px;" id="textMsgView">


					<textarea rows="5" cols="30" class="form-control" placeholder="文字内容"
							style="width: 80%; height: 300px; padding: 5px;display:inline;"
							id="Pcontent"></textarea>


				</div>
				<div style="text-align: center; margin: 20px auto; display: none;"
					id="linkMsgView">

					 
					<div class="input-group" style="margin: 0 auto;">
						<span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>标题:</span>
						<input type="text"  class="form-control"  id="Ptitle" maxlength="16" style="display: inline; width: 500px;height: 45px;" />
					</div>
					
					<div class="input-group" style="margin: 20px auto;">
						<span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>图片:</span>
						<input type="text" placeholder="封面图片网络地址" class="form-control"   id="Pimage" maxlength="200" style="display: inline; width: 500px;height: 45px;" />
					</div>
					
					<div class="input-group" style="margin: 20px auto;">
						<span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>连接:</span>
						<input type="text" placeholder="内容网络链接地址" name="code" class="form-control"  id="Plink" maxlength="200" style="display: inline; width: 500px;height: 45px;" />
					</div>
				 
					<div class="input-group" style="margin: 20px auto;">
						<span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>文字:</span>
						<textarea rows="5" placeholder="内容简要描述" class="form-control" cols="30" style="display: inline; width: 500px; height: 200px; padding: 5px;"id="Ptext"></textarea>
						 
					</div>

				</div>
			</div>
			<div class="modal-footer"
				style="padding: 5px 10px; text-align: center; position: absolute; bottom: 0px; width: 100%;"
				id="doPushTextMessageButton">
				<a type="button" class="btn btn-success btn-lg"
					onclick="doPushTextMessage()" style="width: 200px;"> 发 送</a>
			</div>
			<div class="modal-footer"
				style="padding: 5px 10px; text-align: center; display: none;"
				id="doPushLinkMessageButton">
				<a type="button" class="btn btn-success btn-lg"
					onclick="doPushLinkMessage()" style="width: 200px;">发 送</a>
			</div>
		</div>
	</div>
</div>