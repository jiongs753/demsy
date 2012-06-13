<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="{$keywords}" />
<meta name="description" content="{$description}"/>
<title>{$title}{if $subtitle != null}——{$subtitle}{/if}</title>

<link href="{$stylePath}/demsy.ui.css" rel="stylesheet" type="text/css" media="screen" />
<script src="{$scriptPath}/jquery.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.utils.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.ui.js" type="text/javascript"></script>
<script type="text/javascript">
var ui=null;
</script>

</head>
<style>
#page{
  {if $ui.height != null}height: {$ui.height};{/if}
  position: relative;
}
{foreach from=$ui.styles item="style"}
{$style.cssStyle}{/foreach}
</style>
<body>
<div id="top">{foreach from=$ui.topBlocks item="dataModel"}{assign var="model" value=$dataModel.model}{assign var="block" value=$model.param}
	<div id="block{$block.id}" dataID="{$block.id}" style="{if $model.cssStyle != null}{$model.cssStyle}{/if}" class="{$model.cssClass}{if $block.ajaxLoad} ajaxLoad{/if}">
	{if $block.ajaxLoad}{else}{render ui=$dataModel uiRender=$uiRender}{/if}
	</div>{/foreach}
</div>
<div id="page">{foreach from=$ui.pageBlocks item="dataModel"}{assign var="model" value=$dataModel.model}{assign var="block" value=$model.param}
	<div id="block{$block.id}" dataID="{$block.id}" style="{if $model.cssStyle != null}{$model.cssStyle}{/if}" class="{$model.cssClass}{if $block.ajaxLoad} ajaxLoad{/if}">
	{if $block.ajaxLoad}{else}{render ui=$dataModel uiRender=$uiRender}{/if}
	</div>{/foreach}
</div>
<div id="bottom">{foreach from=$ui.bottomBlocks item="dataModel"}{assign var="model" value=$dataModel.model}{assign var="block" value=$model.param}
	<div id="block{$block.id}" dataID="{$block.id}" style="{if $model.cssStyle != null}{$model.cssStyle}{/if}" class="{$model.cssClass}{if $block.ajaxLoad} ajaxLoad{/if}">
	{if $block.ajaxLoad}{else}{render ui=$dataModel uiRender=$uiRender}{/if}
	</div>{/foreach}
</div>
</body>
</html>
<script type="text/javascript">
$(function(){
	ui = new WebUI({
		loadBlockUrl: "{$loadBlockUrl}",
		dataParam: "{if $dataParam != null}{$dataParam}{/if}",
		contextPath: "{$contextPath}"
	});
});
</script>