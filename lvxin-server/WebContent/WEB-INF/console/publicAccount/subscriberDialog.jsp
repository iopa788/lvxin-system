<%@ page language="java" pageEncoding="utf-8"%>
<%
	String subBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
			
%>
<script type="text/javascript">
    var sPubAccount;
    function showSubscriberDialog(account)
    {
    	$("#userList").empty();
        showProcess('加载中，请稍候......');
        sPubAccount = account;
        $.post("<%=subBasePath%>/console/subscriber/list.action", {account:sPubAccount},
			   function(data){
			   hideProcess();
			   var userList = data.dataList;
			   $("#subscriberDialog").find(".badge").text(userList.length);
			  
			   for(var i = 0;i<userList.length;i+=4)
			   {
			      
			      var line =$("#useritem").clone();
			      $("#userList").append(line);
			      line.removeAttr("id");
			      line.show();
			      
			      buildUserInfoCell(line,userList,i,0);
			      buildUserInfoCell(line,userList,i,1);
			      buildUserInfoCell(line,userList,i,2);
			      buildUserInfoCell(line,userList,i,3);
			   } 
		});
        doShowDialog('subscriberDialog');
    }
  
    function buildUserInfoCell(line,list,listIndex,cellIndex){
    	  if(listIndex+cellIndex>=list.length){
	    	  return;
	      }
	      var cell = line.children().eq(cellIndex);
	      var user = list[listIndex+cellIndex];
	      cell.find(".logo").attr("src","<%=subBasePath%>/files/user-icon/"+user.account);
	      cell.find(".name").text(user.name);
	      cell.find(".account").text(user.account);
	      cell.find(".btn").attr("onclick","showMessageDialog('"+user.account+"')");
	      cell.fadeIn();
    }
    function onUserIconError(obj)
	{
		obj.src="<%=subBasePath%>/resource/img/icon_head_default.png";   
	}
    
    $(document).ready(function(){ 
        $(".draggable").draggable({   
		    handle: ".modal-header",   
		    refreshPositions: false  
		}); 
    });
    
    function showMessageDialog(account)
	{
		$('#messageDialog').modal('show');
		$('#Saccount').val(account);
	}
	function doSendMessage()
	{
		    var message = $('#message').val();
		    var account = $('#Saccount').val();
		    if($.trim(message)=='')
		    {
		       return;
		    }
		    
		    var data ={content:message};
		    
		    showProcess('正在发送，请稍候......');
		    $.post("<%=subBasePath%>/console/message/send.action", {content:JSON.stringify(data),action:201,sender:sPubAccount,receiver:account,format:'0'},
			   function(data){
			   
			      hideProcess();
			      showSTip("发送成功");
			      doHideDialog("messageDialog");
		     });
	}
		  
   </script>
   
	<div class="modal fade" id="subscriberDialog" tabindex="-1" role="dialog" data-backdrop="static" style="overflow-y:auto;">
	<div class="modal-dialog  draggable" style="width: 600px;"  aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header" style="cursor: move;">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">
					订阅者列表<span class='badge' style="margin-top: -3px;background-color: #4da0f5;padding: 4px 9px;"></span>    
				</h4>
			</div>
			<div class="modal-body" style="padding: 0px;overflow-y: auto;width: 100%;height: 700px;" >
               <div class="row" style="border-bottom: 1px solid #eee;display:none;margin:0px;" id="useritem">
                   <div class="col-md-3" style="text-align: center;margin: 10px 0px;">
		               <img onerror='onUserIconError(this)' style="border-radius: 100px;" class="logo" height="50px" width="50px"/>
		               <div class="name" style="font-size: 14px;color: #8a8a8a;padding: 3px;font-weight: blod;"></div>
		               <div class="account" style="font-size: 14px;color: #248ac7;padding: 3px"></div>
		               <button type="button" class="btn btn-primary" style="font-size: 12px;border-radius: 15px;padding: 5px;margin-top: 5px;">发送消息</button>
	               </div>
	               <div class="col-md-3" style="text-align: center;margin: 10px 0px;display: none;">
		               <img onerror='onUserIconError(this)' style="border-radius: 100px;" class="logo" height="50px" width="50px"/>
		               <div class="name" style="font-size: 14px;color: #8a8a8a;padding: 3px;font-weight: blod;"></div>
		               <div class="account" style="font-size: 14px;color: #248ac7;padding: 3px"></div>
		               <button type="button" class="btn btn-primary" style="font-size: 12px;border-radius: 15px;padding: 5px;margin-top: 5px;">发送消息</button>
	               </div>
	               <div class="col-md-3" style="text-align: center;margin: 10px 0px;display: none;">
		               <img onerror='onUserIconError(this)' style="border-radius: 100px;" class="logo" height="50px" width="50px"/>
		               <div class="name" style="font-size: 14px;color: #8a8a8a;padding: 3px;font-weight: blod;"></div>
		               <div class="account" style="font-size: 14px;color: #248ac7;padding: 3px"></div>
		               <button type="button" class="btn btn-primary" style="font-size: 12px;border-radius: 15px;padding: 5px;margin-top: 5px;">发送消息</button>
	               </div>
	               <div class="col-md-3" style="text-align: center;margin: 10px 0px;display: none;">
		               <img onerror='onUserIconError(this)' style="border-radius: 100px;" class="logo" height="50px" width="50px"/>
		               <div class="name" style="font-size: 14px;color: #8a8a8a;padding: 3px;font-weight: blod;"></div>
		               <div class="account" style="font-size: 14px;color: #248ac7;padding: 3px"></div>
		               <button type="button" class="btn btn-primary" style="font-size: 12px;border-radius: 15px;padding: 5px;margin-top: 5px;">发送消息</button>
	               </div>
               </div>
               <div id="userList">
               </div>
			</div>
		</div>
     </div>
  </div>
     
     <div class="modal fade" id="messageDialog" tabindex="-1" role="dialog" >
		<div class="modal-dialog draggable" style="width: 420px;">
			<div class="modal-content">
				<div class="modal-header" style="cursor: move;">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                <h4 class="modal-title">发送消息</h4>
				</div>
				<div class="modal-body">
					<div class="form-groupBuy">
							<label for="Amobile">
								接收账号:
							</label>
							<input type="text" class="form-control" id="Saccount"
								name="account"
								style="width: 100%; font-size: 20px; font-weight: bold;height:40px;"
								disabled="disabled" />
						</div>
						<div class="form-groupBuy" style="margin-top: 20px;">
							<label for="exampleInputFile">
								消息内容:
							</label>
							<textarea rows="10" style="width: 100%; height: 200px;"
								id="message" name="message" class="form-control"></textarea>
						</div>
				</div>
				<div class="modal-footer" style="padding: 5px 10px; text-align: center;">
					<button type="button" class="btn btn-success btn-lg" style="width: 200px;" onclick="doSendMessage()">
						<span class="glyphicon glyphicon-send" style="top:2px;"></span> 发送
					</button>
				</div>
			</div>
		</div>
</div>
     
     