var isFristBind = true;
function doLogin() {
	var account = $("#account").val();
	var password = $("#password").val();
	if ($.trim(password) == '' || $.trim(account) == '') {
		return;
	}
	showProcess('正在登录，请稍候......');
    $.post("/web/client/login.api", {account:account,password:password},
	   function(data){
	    if(data.code==403){
	    	hideProcess();
		    showETip("帐号或者密码不正确");
		    return 
	    }
	    if(data.code==405){
	    	hideProcess();
		    showETip("当前账号已经被禁用");
		    return 
	    }
	    if(data.code==200){
	    	window.localStorage.account = account;
	    	CIMWebBridge.connection();
	    }
     });
}

/**  当socket连接成功回调 **/
function onConnectionSuccessed()
{
	 var uuid = window.localStorage.uuid;
	 var account = window.localStorage.account;
	 if(uuid =='' || uuid == undefined){
		 uuid = generateUUID();
		 window.localStorage.uuid = uuid;
	 }
	 CIMWebBridge.bindAccount(account,uuid);
}

/** 当收到请求回复时候回调  **/
function onReplyReceived(json)
{
  if(json.key=='client_bind' && json.code==200 && isFristBind)
  {
	 isFristBind = false;
     hideProcess();
     $('#LoginDialog').fadeOut();
     initLayim();
  }
}

/** 当收到消息时候回调  **/

function onMessageReceived(message)
{ 
	parent.showWebClientDialog();
	if(message.action == '444' || message.action == '999' ){
		CIMWebBridge.stop();
		window.location.reload();
		return;
	}
	notifyReceivedMessage(message);
}
 function generateUUID() {
	 var d = new Date().getTime();
     return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	   var r = (d + Math.random()*16)%16 | 0;
	   d = Math.floor(d/16);
	   return (c=='x' ? r : (r&0x3|0x8)).toString(16);
	 });
}