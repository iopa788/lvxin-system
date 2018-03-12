<#macro pager page>
<div style="float: left;line-height: 40px;margin-left: 10px;">共${page.totalElements}条记录,${page.totalPages}页</div>
<#if page.totalPages gt 1>  

			<ul class="pagination" style="margin: 5px 0;">
			<#if page.first>
			  <li  class="disabled"><a>&laquo;</a></li>
			<#else>
			  <li style='cursor: pointer;'><a onclick='gotoPage(1)'>&laquo;</a></li>
			  <li style='cursor: pointer;'><a onclick="gotoPage(${page.number})">上一页</a></li>
			</#if>
			
			<#list page.number - 5..page.number+6 as i>
			  <#if i gte 0 && i lte page.totalPages -1>
			     <#if i == page.number>
			        <li class="active"><a >${i+1}</a></li>
			     <#else>
			        <li style='cursor: pointer;'><a onclick='gotoPage(${i}+1)'>${i+1}</a></li>
			     </#if>
			  </#if>
			</#list>
			 
			<#if page.last>
				<li class="disabled"><a>&raquo;</a></li>
			<#else>
			    <li style='cursor: pointer;'><a onclick="gotoPage(${page.number+2})">下一页</a></li>
			    <li style='cursor: pointer;'><a onclick="gotoPage(${page.totalPages})">&raquo;</a></li>
			</#if>
			<li><input   type="number" id='GoPageNumber' style='padding:6px;width: 60px;height: 31px;' class="input-pagenumber form-control" ><a style='float: right;cursor: pointer;' onclick="gotoPage($('#GoPageNumber').val())">GO</a></li>
		</ul>
</#if>
</#macro>
