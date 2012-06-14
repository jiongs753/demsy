<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>{$title}{if $subtitle != null}{$subtitle}{/if}</title>

<link href="{$stylePath}/demsy.ui.css" rel="stylesheet" type="text/css" media="screen" />

<script src="{$scriptPath}/jquery.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.utils.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.ui.js" type="text/javascript"></script>

<!-- 排版必须 -->
<link href="{$stylePath}/jquery.ui.core.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.theme.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.resizable.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.dialog.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.button.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.spinner.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.uploadify.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery-jvert-tabs.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/demsy.bizform.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/demsy.colorpicker.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$scriptPath}/accordion/demsy.accordion.css" rel="stylesheet" type="text/css" media="screen" />

<script src="{$scriptPath}/jquery.ui.core.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.widget.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.mouse.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.draggable.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.droppable.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.position.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.resizable.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.dialog.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.button.js" type="text/javascript"></script>
<script src="{$scriptPath}/accordion/demsy.accordion.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.spinner.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.colorpicker.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.contextmenu.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.uploadify.js" type="text/javascript"></script>
<script src="{$scriptPath}/swfobject.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.popup.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery-jvert-tabs.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.bizmodule.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.admin.ui.js" type="text/javascript"></script>
</head>
<body pageID="{$ui.id}" styleID="{if $ui.param.style != null}{$ui.param.style.id}{/if}">
<!-- style -->
{foreach from=$ui.styles item="style"}
<div id="style{if $style.id != null}{$style.id}{else}{$style.cssClass}{/if}" dataID="{if $style.id != null}{$style.id}{else}{$style.cssClass}{/if}">
{import file="Style.st" style=$style}</div>{/foreach}

<style>
#top{
  border-bottom: 1px dotted #920706;
}
#page{
  height: {$ui.height};
  border-left: 1px dotted #920706;
  border-right: 1px dotted #920706;
}
#bottom{
  border: 1px dotted #920706;
}
.droppable-hover{
    background: #e17009;
}
.droppable-active{
    background: #e17009;
}
.ui-droppable{
  padding-bottom: 100px;
}
.box{
  border: 1px dashed #336699;
}
.selectedBlock{
  border: 1px solid #336699;
}
.block{
  padding-bottom: 0;
}
.ui-dialog, .ui-dialog .ui-widget-content, .ui-dialog ul, .ui-dialog li ul{
  	overflow: visible;
	padding: 2px 1px; 
}
.ui-tabs .ui-tabs-panel{
	padding: 3px 0; 
}
#dialogUiModelLib .ui-tabs {
	border: 0px solid #79b7e7;
}
.ui-tabs .ui-tabs-nav li a { 
 padding: 5px; 
}
</style>

<div id="top" class="droppable area">{import file="_block_admin.st" children=$ui.topBlocks}</div>
<div id="page" class="droppable area">{import file="_block_admin.st" children=$ui.pageBlocks}</div>
<div id="bottom" class="droppable area">{import file="_block_admin.st" children=$ui.bottomBlocks}</div>

<div id="dialog"></div>
<div id="dialogUiModelLib"></div>
<div style="display:none;z-index:9999;" id="blockContextMenu">
	<ul>
        <li id="createBlock"><img src='{$contextPath}/themes2/images/actionlib/c.gif' />&nbsp;添加板块</li>
    	<li id="editBlock"><img src='{$contextPath}/themes2/images/actionlib/e.gif' />&nbsp;编辑板块</li>
        <li id="deleteBlock"><img src='{$contextPath}/themes2/images/actionlib/d.gif' />&nbsp;删除板块</li>
        <li id="editStyle"><img src='{$contextPath}/themes2/images/actionlib/e.gif' />&nbsp;编辑样式</li>
    </ul>
</div>
<div style="display:none;z-index:9999;" id="pageContextMenu">
	<ul>
        <li id="createBlock"><img src='{$contextPath}/themes2/images/actionlib/c.gif' />&nbsp;添加板块</li>
        <li id="showBox"><img src='{$contextPath}/themes2/images/actionlib/layout.gif' />显示边框</li>
        <li id="hideBox"><img src='{$contextPath}/themes2/images/actionlib/layout.gif' />隐藏边框</li>
    	<li id="editPage"><img src='{$contextPath}/themes2/images/actionlib/e.gif' />&nbsp;编辑模版</li>
    	<li id="editStyle"><img src='{$contextPath}/themes2/images/actionlib/e.gif' />&nbsp;编辑样式</li>
        <li id="createStyle"><img src='{$contextPath}/themes2/images/actionlib/c.gif' />&nbsp;添加样式</li>
    	<li id="createWebCata"><img src='{$contextPath}/themes2/images/actionlib/c.gif' />&nbsp;添加栏目</li>
    	<li id="createWebCont"><img src='{$contextPath}/themes2/images/actionlib/c.gif' />&nbsp;录入信息</li>
    </ul>
</div>

<script type="text/javascript">
$(function(){
	var ui = new WebUI({
		loadBlockUrl: "{$loadBlockUrl}",
        dataParam: "{$dataParam}"
	});
	demsyUIManager.create({
		webUI: ui,
		loadPageUrl: "{$loadPageUrl}",
		loadUilibUrl: "{$loadUilibUrl}",
		//block
		createUrl: "{$createUrl}",
		editUrl: "{$editUrl}",
		saveUrl: "{$saveUrl}",
		deleteUrl: "{$deleteUrl}",
		loadUrl: "{$loadUrl}",
		//style
		loadStyleUrl: "{$loadStyleUrl}",
		createStyleUrl: "{$createStyleUrl}",
		editStyleUrl: "{$editStyleUrl}",
		saveStyleUrl: "{$saveStyleUrl}",
		//page
		editPageUrl: "{$editPageUrl}",
		savePageUrl: "{$savePageUrl}",
		//web
		createWebCataUrl: "{$createWebCataUrl}",
		saveWebCataUrl: "{$saveWebCataUrl}",
		createWebContUrl: "{$createWebContUrl}",
		//
		pageID: "{$pageID}",
		contextPath: "{$contextPath}"
	});
});
</script>
</body>
</html>
