{if $data.size > 0}
<script src="{$scriptPath}/jquery.scrollable.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.menu.js" type="text/javascript"></script>

<link href="{$stylePath}/demsy.hmenu.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/demsy.vmenu.css" rel="stylesheet" type="text/css" media="screen" />
<style>
.hmenu_panel {
	font-size: {$fontSize};
}
</style>
<div id="{$ui.uiid}" style="z-index: 999; height: 27px;"></div>
<script type='text/javascript'>
bizmodule.addTMenu({
	moduleID: "{$moduleID}",
	uiid: "{$ui.uiid}",
	items: {if $data !=null}{import file="ToolbarMenu.json.st" data=$data}{else}'{$ui.dataUrl}'{/if},
	iconPath: '{$imagePath}/tmenu',
	vWidth: 180,
	contextPath: '{$contextPath}',
	type: '{$type}'
});
</script>
{/if}