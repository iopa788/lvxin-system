var mLayim;
function initLayim() {
	layui.use('layim',function(layim) {
		mLayim = layim;
		layim.config({

							// 初始化接口
							init : {
								url : '/web/client/main.api',
								data : {
									account : window.localStorage.account
								}
							}

							// 查看群员接口
							,
							members : {
								url : '/web/client/members.api',
								data : {}
							}
							// 扩展工具栏
							,
							initSkin : '5.jpg' // 1-5 设置初始背景
							,
							notice : true
						// 是否开启桌面消息提醒，默认false
						});

						// 监听发送消息
						layim.on('sendMessage',
								function(data) {
							       sendMessage(data);
								});

					});
}
 
function changeOnlineState(id,state){
	if(id == window.localStorage.account){
		return ;
	}
	mLayim.setFriendStatus(id, state);
}
function notifyReceivedMessage(message){
	var type ;
	if(message.action=='0'){
		type ='friend';
	}else if(message.action=='3'){
		type ='group';
	}else if(message.action=='900')
	{
		changeOnlineState(message.content,"offline");
		return;
	}else if(message.action=='901')
	{
		changeOnlineState(message.content,"online");
		return;
	}else
	{
		return ;
	}
	if(message.format=='1'){
		var image = JSON.parse(message.content);
		message.content = "img["+"/files/other-files/"+image.image+"]"
	}
	if(message.format=='2'){
		var voice = JSON.parse(message.content);
		message.content = "audio["+"/files/other-files/"+voice.key+"]"
	}
	if(message.format=='3'){
		var file = JSON.parse(message.content);
		message.content = "file("+"/files/other-files/"+file.key+")["+file.name+"]"
	}
	if(message.format=='8'){
		var video = JSON.parse(message.content);
		message.content = "video["+"/files/other-files/"+video.video+"]"
	}
	if(message.format=='4'){
		message.content = "不支持地图类型消息，请在手机上查看"
	}
	var from = type =='friend' ? findFriend(message.sender):findFriend(message.title);
	var fromMessage = {
			  username: from.username //消息来源用户名
				  ,avatar: "/files/user-icon/"+from.id //消息来源用户头像
				  ,id: message.sender //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
				  ,type: type //聊天窗口来源类型，从发送消息传递的to里面获取
				  ,content: message.content //消息内容
				  ,cid: message.mid //消息id，可不传。除非你要对消息进行一些操作（如撤回）
				  ,mine: false //是否我发送的消息，如果为true，则会显示在右方
				  ,fromid: message.title //消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
				  ,timestamp: message.timestamp //服务端时间戳。注意：JS中的时间戳是精确到毫秒，如果你返回的是标准的 unix 时间戳，记得要 *1000
				};
	mLayim.getMessage(fromMessage);
	
	receiptMessage(message.mid);
}
function findFriend(account){
	var friendList = mLayim.cache().friend[0].list;
	for(var i = 0;i<friendList.length;i++)
	{
	   var friend = friendList[i];
	   if(friend.id == account){
			  return friend;
	   }
	}
	
	return {};
}
function findGroup(id){
	var groupList = mLayim.cache().group;
	
	for(var i = 0;i<groupList.length;i++)
	{
	   var group = groupList[i];
	   if(group.id == id){
		 return group;
	   }
	}
	
	return {};
}
function sendMessage(data){
	var sender = data.mine.id;
	var receiver =  data.to.id;
	var action =  data.to.type == 'friend' ? 0 : 1;
	var content =  data.mine.content;
	$.post("/web/client/send.api", {content:content,action:action,sender:sender,receiver:receiver,format:'0'},
		function(data){
	});
}

function receiptMessage(mid){
	$.post("/web/client/receipt.api", {mid:mid},
		function(data){
	});
}