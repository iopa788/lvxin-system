<%@ page language="java" pageEncoding="utf-8"%>
<%
	String menuBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
			
%>
<script type="text/javascript">
    var editMode;
    var menuId;
    var pubAccount;
    var menuList;
    var delete_button = "<button type='button' onclick='doDeleteMenu(this)' class='btn btn-danger delete-icon' style='position: absolute; left: 0px; top: 0px; height: 41px; width: 42px; border-radius: 0px; font-size: 16px;display:none;'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> </button>";
    function doSaveMenu()
	{
	       
	        if(editMode == 'add'){
	           doAddMenu();
	           return;
	        }
	        
	        var vaild = true;
            var params = {};
            params.account = pubAccount;
            params.gid = menuId;
	        $(".menu-form:visible").find(".form-control:visible").each(function(i,n){
			     if($.trim($(n).val())=='')
			     {
			       $(n).parent().fadeOut().fadeIn();
			       vaild = false ;
			     }
			     params[$(n).attr("name")] = $(n).val(); 
			});
			
			if(!vaild){
			  return ;
			}
			
			
		    showProcess('正在保存，请稍候......');
		    $.post("<%=menuBasePath%>/console/publicMenu/update.action",params,
			   function(data){
			      hideProcess();
			      
			      if(200==data.code)
			      {        
			             showSTip("修改成功");
			             var item = $("#"+menuId);
			             item.attr("content",params.content);
				         item.attr("name",params.name);
				         item.find(".name").text(params.name);
				         item.find(".badge").text(params.sort);
				         item.attr("link",params.link);
				         item.attr("code",params.code);
				         item.attr("type",params.type);
				         item.attr("sort",params.sort);
				         
			      }
			      if(data.code==403)
			      {
			          showETip("菜单KEY重复");
			      }
		     });
	}
 
 
 
    function doAddMenu(){
    
            var vaild = true;
            var params = {};
            params.account = pubAccount;
            
            $(".menu-form:visible").find(".form-control:visible").each(function(i,n){
			     if($.trim($(n).val())=='')
			     {
			       $(n).parent().fadeOut().fadeIn();
			       vaild = false ;
			     }
			     params[$(n).attr("name")] = $(n).val(); 
			});
			
			if($(".root_menu_item.active").length > 0){
			     params.fid =$(".root_menu_item.active").eq(0).attr("gid"); 
			}
			if(!vaild){
			  return ;
			}
	        
		    showProcess('正在保存，请稍候......');
		    $.post("<%=menuBasePath%>/console/publicMenu/add.action",params,
			   function(data){
			      hideProcess();
			      if(data.code==200)
			      {
			          showSTip("保存成功");
			          var gid = data.data;
			          if(params.fid == undefined)
			          {
					      var line = "<div onclick=onRootMenuClicked('"+gid+"') sort='"+params.sort+"' class='list-group-item root_menu_item' style=' cursor: pointer;border-top: 0px;border-radius: 0px;border-left: 0px;border-right: 0px;margin-bottom: 0px;' content='"+params.content+"' id='"+gid+"' name='"+params.name+"'  link='"+params.link+"' code='"+params.code+"' type='"+params.type+"'  gid='"+gid+"'  >"+delete_button+"<span class='name'>"+params.name+"</span><span class='badge'>"+params.sort+"</span></div>";
					      $("#rootMenuList").append(line);
					      onRootMenuClicked(gid);
					  }else
					  {
					      var line = "<div content='"+params.content+"' sort='"+params.sort+"'  id='"+gid+"' name='"+params.name+"'  link='"+params.link+"' code='"+params.code+"' type='"+params.type+"' fid='"+params.fid+"' gid='"+gid+"' onclick=onSubMenuClicked('"+gid+"')     class='list-group-item  child_menu_item' style='border-radius: 0px;border-left: 0px;border-top: 0px;border-right: 0px;cursor: pointer;margin-bottom: 0px;' >"+delete_button+"<span class='name'>"+params.name+"</span><span class='badge'>"+params.sort+"</span></div>";
				          $("#childMenuList").append(line);
					      onSubMenuClicked(gid);
					  }
			      }
			      if(data.code==403)
			      {
			          showETip("菜单KEY重复");
			      }
		     });
    
    
    }
    function  doDeleteMenu(item)
    {
            var id =  $(item).parent().attr("gid");
            var setting = {hint:"删除后无法恢复,确定删除这个菜单吗?",
		                    onConfirm:function(){
		                      showProcess('正在删除，请稍候......');
		                      $.post("<%=menuBasePath%>/console/publicMenu/delete.action", {account:pubAccount,gid:id},
							  function(data){
							      doHideConfirm();
							      hideProcess();
							      showSTip("删除成功");
					              $('.menu-form').hide();
					              $("#saveMenuButton").attr("disabled","disabled");
					              if($("#"+id).attr('type')=='0')
					              {
					                 $("#childMenuList").empty();
					                 $("#addSubMenuButton").attr("disabled","disabled");
					              }
					              $('#'+id).fadeOut().remove();
						      });
		                     
		                    }};
		     
		     doShowConfirm(setting);
    }
    function  onRootMenuClicked(gid)
    {       
         menuId = gid;
         editMode = 'update';
         var item = $("#"+gid);
         $("#rootMenuList").find(".delete-icon").hide();;
         item.find(".delete-icon").show();;
         $(".root_menu_item").removeClass("active");
	     item.addClass("active");
         $('.menu-form').hide();
         $('.root-menu-form').show();
         $(".root-menu-form").find(".form-control").each(function(i,n){
			   if(item.attr($(n).attr("name"))!='undefined'){
			   	 $(n).val(item.attr($(n).attr("name")));
			   }
		 });
		 
		 $("#rootMenuType").change();
	      
	     $("#childMenuList").empty();
	     $("#saveMenuButton").removeAttr("disabled");
		 if(item.attr("type")==0)
		 {
		        $("#addSubMenuButton").removeAttr("disabled");
		        
		        $.post("<%=menuBasePath%>/console/publicMenu/childList.action", {fid:gid},
				   function(data){
					   $("#addSubMenuButton").removeAttr("disabled");
					   $("#childMenuList").show();
					   var menuList = data.dataList;
					   for(var i = 0;i<menuList.length;i++)
					   {
					      var menu = data.dataList[i];
					      var line = "<div content='"+menu.content+"' id='"+menu.gid+"' name='"+menu.name+"' sort='"+menu.sort+"'  link='"+menu.link+"' code='"+menu.code+"' type='"+menu.type+"' fid='"+menu.fid+"' gid='"+menu.gid+"' onclick=onSubMenuClicked('"+menu.gid+"')    class='list-group-item  child_menu_item' style='cursor: pointer;border-top: 0px;border-radius: 0px;border-left: 0px;border-right: 0px;margin-bottom: 0px;' >"+delete_button+"<span class='name'>"+menu.name+"</span><span class='badge'>"+menu.sort+"</span></div>";
					      $("#childMenuList").append(line);
					   }
				   
		       });
		 }else
		 {
		    $("#addSubMenuButton").attr("disabled","disabled");
		 }
		   
    }
    function  onSubMenuClicked(gid)
    {
                menuId = gid;
                editMode = 'update';
                var item = $("#"+gid);
                $('.menu-form').hide();
                $('.sub-menu-form').show();
                $("#childMenuList").find(".delete-icon").hide();
                item.find(".delete-icon").show();;
                $(".child_menu_item").removeClass("active");
                item.addClass("active");
                $(".sub-menu-form").find(".form-control").each(function(i,n){
			         if(item.attr($(n).attr("name"))!='undefined'){
					   	 $(n).val(item.attr($(n).attr("name")));
					   }
			    });
			    $("#subMenuType").change();
			    $("#saveMenuButton").removeAttr("disabled");
                
    }
    function onAddSubMenuClick()
    {
         var item = $("#rootMenuList").find(".active");
         
         if($("#childMenuList").children().length==5)
         {
            showHTip("最多只能添加5个二级菜单");
            return ;
         }
       
         editMode = 'add';
         $('.menu-form').hide();
         $('.sub-menu-form').show();
         $('.sub-menu-form').find(".input-group").hide();
         $('.sub-menu-form').find(".base-attr").show();
         $('.sub-menu-form')[0].reset()
         $(".child_menu_item").removeClass("active");
         $("#childMenuList").find(".delete-icon").hide();;
         $("#saveMenuButton").removeAttr("disabled");
    }
    function showMenuEditDialog(paccount)
    {
        $(".menu-form").hide();
        $("#addSubMenuButton").attr("disabled","disabled");
        $("#saveMenuButton").attr("disabled","disabled");
        $("#rootMenuList").empty();
        $("#childMenuList").empty();
        showProcess('加载中，请稍候......');
        pubAccount = (paccount);
        $.post("<%=menuBasePath%>/console/publicMenu/rootList.action", {account:paccount},
			   function(data){
			   hideProcess();
			   menuList = data.dataList;
			   for(var i = 0;i<menuList.length;i++)
			   {
			      var menu = data.dataList[i];
			      var line = "<div onclick=onRootMenuClicked('"+menu.gid+"') class='list-group-item root_menu_item' style='cursor: pointer;border-radius: 0px;border-left: 0px;border-right: 0px;border-top: 0px;margin-bottom: 0px;'  sort='"+menu.sort+"'   content='"+menu.content+"' id='"+menu.gid+"'  name='"+menu.name+"' link='"+menu.link+"' code='"+menu.code+"' type='"+menu.type+"' fid='"+menu.fid+"' gid='"+menu.gid+"'>"+delete_button+"<span class='name'>"+menu.name+"</span><span class='badge'>"+menu.sort+"</span></div>";
			      $("#rootMenuList").append(line);
			   }
			      
		});
        doShowDialog('MenuEditDialog');
    }
    
    function onAddRootMenuClick()
    {
         if($("#rootMenuList").children().length==3)
         {
            showHTip("最多只能添加3个一级菜单");
            return ;
         }
         editMode = 'add';
         $("#rootMenuList").find(".delete-icon").hide();;
         $('.menu-form').hide();
         $('.root-menu-form').show();
         $('.root-menu-form').find(".input-group").hide();
         $('.root-menu-form').find(".base-attr").show();
         $('.root-menu-form')[0].reset()
         $(".root_menu_item").removeClass("active");
         $("#childMenuList").hide();
         $("#saveMenuButton").removeAttr("disabled");
        
    }
    $(document).ready(function(){ 
        $(".draggableDialog").draggable({   
		    handle: ".modal-header",   
		    refreshPositions: false  
		}); 
	    $("#rootMenuType").change(function(){
	        var domain = $(this).find("option:selected").attr("domain");
	        $('.root-menu-form').find(".input-group").hide();
	        $('.root-menu-form').find(".base-attr").show();
	        $('.root-menu-form').find("." + domain).show();
	    });
	    $("#subMenuType").change(function(){
	        var domain = $(this).find("option:selected").attr("domain");
	        $('.sub-menu-form').find(".input-group").hide();
	        $('.sub-menu-form').find(".base-attr").show();
	        $('.sub-menu-form').find("." + domain).show();
	    });
    });
    
   </script>
   
	<div class="modal fade" id="MenuEditDialog" tabindex="-1" role="dialog" data-backdrop="static" style="overflow-y:auto;">
	<div class="modal-dialog draggableDialog" style="width: 760px;"  aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header" style="cursor: move;">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="feafaecacvvvv">
					菜单配置    
				</h4>
			</div>
			<div class="modal-body" style="text-align: center;padding: 0px;">
 
	
			    <div  style="width: 180px;border-right: 1px solid #e7e7eb;height: 500px;float: left;border-bottom-right-radius: 6px;">
		  			    <div style="height:100%;margin-top:5px" id="rootMenuList"></div>
			  			<div style="text-align: center;width:179px;border-bottom-left-radius: 6px;background-color: #f4f5f9;bottom: 0px;border-top: 1px solid #ddd;padding: 10px;position: absolute;">
							<button   class="btn btn-success" style="font-size: 12px;" onclick="onAddRootMenuClick()">
								<span class="glyphicon glyphicon-plus"></span>一级菜单
							</button>
						</div>
				</div>
				<div  style="width: 180px;height: 500px;float: left;border-right: 1px solid #e7e7eb;" id="childMenuPanel">
		                <div  id="childMenuList" style="height:100%;margin-top:5px"></div>
		                <div style="text-align: center;width:179px;background-color: #f4f5f9;bottom: 0px;border-top: 1px solid #ddd;padding: 10px;position: absolute;">
							<button  id="addSubMenuButton" class="btn btn-success" style="font-size: 12px;" onclick="onAddSubMenuClick()">
								<span class="glyphicon glyphicon-plus"></span>二级菜单
							</button>
						</div>
					 			  			    
				</div>
				<div  style="width: 400px;height: 500px;margin-left: 360px;" id="menuCreateView">
		               <form class="menu-form root-menu-form" style = "padding: 26px;display:none;">
								
								
								<div class="input-group base-attr">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>排序</span>
								  <input type="number" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'').replace(/\./g,'')" value = "1" name="sort" maxlength="6" style="display: inline; width: 300px;height: 40px;" />
								</div>
								
								<div class="input-group base-attr"  style="margin-top: 20px">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>KEY</span>
								  <input type="text" name="code" class="form-control"  maxlength="12" style="display: inline; width: 300px;height: 40px;" />
								</div>
								
								<div class="input-group base-attr"  style="margin-top: 20px">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>名称</span>
								  <input type="text" name="name" class="form-control"  maxlength="12" style="display: inline; width: 300px;height: 40px;" />
								</div>
		                        <div class="input-group base-attr"  style="margin-top: 20px">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>类型</span>
								  <select name="type"  class="form-control" id="rootMenuType"    style="width:300px; height: 40px;display: inline;">
									 <option value="0" domain="base-attr" >一级菜单</option>
									 <option value="1" domain="base-attr" >调用接口</option>
									 <option value="2" domain="url-attr"  >网页地址</option>
									 <option value="3" domain="text-attr" >回复文字</option>
									</select>
								</div>
								
								<div class="input-group url-attr"  style="margin-top: 20px;display:none;">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>网址</span>
								  <input type="text" class="form-control" name="link" maxlength="200" style="display: inline; width: 300px;height: 40px;" />
								</div>
								
								<div class="input-group text-attr"  style="margin-top: 20px;display:none;">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>文字</span>
									<textarea rows="5" cols="30" class="form-control" style="display: inline; width:300px;height:150px;padding: 5px;" name="content"></textarea>
								</div>
								
								
					</form>
					<form class="menu-form sub-menu-form" style = "padding: 26px;display:none;">
								
								
								<div class="input-group base-attr">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>排序</span>
								  <input type="number" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'').replace(/\./g,'')"  value = "1" name="sort" maxlength="6" style="display: inline; width: 300px;height: 40px;" />
								</div>
								
								<div class="input-group base-attr"  style="margin-top: 20px">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>KEY</span>
								  <input type="text" name="code" class="form-control"  maxlength="32" style="display: inline; width: 300px;height: 40px;" />
								</div>
								<div class="input-group base-attr"  style="margin-top: 20px">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>名称</span>
								  <input type="text" name="name" class="form-control"  maxlength="12" style="display: inline; width: 300px;height: 40px;" />
								</div>
		                        <div class="input-group base-attr"  style="margin-top: 20px">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>类型</span>
								  <select name="type"  class="form-control" id="subMenuType" style="width:300px; height: 40px;display: inline;">
									 <option value="1" domain="base-attr" >调用接口</option>
									 <option value="2" domain="url-attr"  >网页地址</option>
									 <option value="3" domain="text-attr" >回复文字</option>
									</select>
								</div>
								
								
								<div class="input-group url-attr"  style="margin-top:20px;display:none;">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>网址</span>
								  <input type="text" class="form-control" name="link" maxlength="200" style="display: inline; width: 300px;height: 40px;" />
								</div>
								
								<div class="input-group text-attr"  style="margin-top:20px;display:none;">
								  <span class="input-group-addon" style="padding:0px;width: 60px;"><font color="red">*</font>文字</span>
									<textarea rows="5" cols="30" class="form-control" style="display: inline; width:300px;height:150px;padding: 5px;" name="content"></textarea>
								</div>
					</form>
					
					<div style="text-align: center;width:398px;border-bottom-right-radius: 6px;background-color: #f4f5f9;bottom: 0px;border-top: 1px solid #ddd;padding: 6.5px;position: absolute;">
						<button  id="saveMenuButton" class="btn btn-primary" style="padding:8px 24px;" onclick="doSaveMenu()">
							<span class="glyphicon glyphicon-ok-circle"></span>保 存
						</button>
				    </div>
			</div>
		</div>
	</div>		
	</div>
</div>