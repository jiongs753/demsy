<table id="{$ui.uiid}"></table>
<div id="pg{$ui.uiid}"></div>
<script type='text/javascript'>
bizmodule.addGrid({
	uiid: "{$ui.uiid}",
	moduleID: "{$moduleID}",{if $comboboxID != null}
	comboboxID: "{$comboboxID}",
	comboboxType: "{$comboboxType}",{/if}
	url:'{$ui.dataUrl}'{if $postData != null}
	,postData: {$postData}{/if}
	,datatype:'{$ui.dataType}'
	,mtype: 'POST'
	,colNames:["ID"{foreach from=$ui.fields item="fld"},"{$fld.label}"{/foreach}]
	,colModel:[{ 
		  name:'{$ui.idField}',index:'{$ui.idField}',width:50,align:'left',hidden: {if $comboboxID == null}false{else}true{/if}
		}{foreach from=$ui.fields item="fld"},{ 
		  name:'{$fld.name}',index:'{$fld.propName}',width:{$fld.width},align:'{$fld.align}'
		  	{if $fld.hidden}
				,hidden:{$fld.hidden},
		  		searchoptions:{
			  		searchhidden:{$fld.searchhidden}
				}
			{/if}
		}{/foreach}
	]
	,rowNum:{$ui.rowNum}
	,height:{$height}
	,autowidth:{$ui.autowidth}
	,width:{$ui.width}
	,multikey:'{$ui.multikey}'
	,viewrecords:{$ui.viewrecords}
	,multiselect:{$ui.multiselect}
	,multiboxonly:{$ui.multiboxonly}
	,altRows: false{if $ui.rowList != null}
	,rowList: {$ui.rowList}{/if}
	,shrinkToFit:{$ui.shrinkToFit}
	//,rownumbers:{$ui.rownumbers}
	//,rownumWidth: 40
	,resizeclass:'{$ui.resizeclass}'
	,gridview: true
	,search: {$ui.search}
	,sortname: '{$ui.sortname}'
    ,sortorder: '{$ui.sortorder}'
	,pager:"#pg{$ui.uiid}"
});
</script>
