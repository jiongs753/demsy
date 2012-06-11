<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>{$title}</title>

<link href="{$stylePath}/demsy.ui.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.core.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.theme.css" rel="stylesheet" type="text/css" media="screen" />

<script src="{$scriptPath}/jquery.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.ui.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.core.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.widget.js" type="text/javascript"></script>

<style>
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