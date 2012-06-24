<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>{$title}</title>

<link href="{$stylePath}/demsy.ui.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.core.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.theme.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/demsy.colorpicker.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.uploadify.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.datepicker.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.dropdownselect.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.spinner.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/demsy.bizform.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.tabs.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$scriptPath}/accordion/demsy.accordion.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.jqgrid.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/demsy.bizform.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.tree.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.accordion.css" rel="stylesheet" type="text/css" media="screen" />

<script src="{$scriptPath}/jquery.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.utils.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.ui.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.core.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.widget.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.tabs.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.popup.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.spinner.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.popup.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.dropdownselect.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.dropdownselect-zh-CN.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.uploadify.js" type="text/javascript"></script>
<script src="{$scriptPath}/swfobject.js" type="text/javascript"></script>
<script src="{$scriptPath}/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="{$scriptPath}/ckfinder/ckfinder.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.colorpicker.js" type="text/javascript"></script>
<script src="{$scriptPath}/accordion/demsy.accordion.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.grid-zh-CN.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.jqgrid.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.tree.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.accordion.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.bizmodule.js" type="text/javascript"></script>

<style>
.ui-widget,.ui-jqgrid,.ui-jqdialog {
	font-size: 12px;
}
.ui-widget-content .ui-priority-secondary td{
	background: #eee;
}
.bbit-tree-node-el {
	padding: 1px;
}
.main_bg{
	background-image: url({$contextPath}/themes2/images/index_main_bg.jpg);
	background-repeat: repeat;
	background-position: left top;
}
#page{
  width: {if $ui.width != null && $ui.width > 0}{$ui.width}px{else}100%{/if};
  height: 100%;
}
</style>
</head>
<body>
<div id="page">{foreach from=$ui.pageBlocks item="dataModel"}{assign var="model" value=$dataModel.model}
{render ui=$dataModel uiRender=$uiRender}{/foreach}
</div>
</body>
</html>