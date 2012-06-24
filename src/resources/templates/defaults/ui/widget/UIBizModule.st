<div class="main_bg1" id="{$ui.uiid}" style="visibility: hidden;">
	<ul>
		<li><a hideFocus href="#tab{$ui.id}">{$ui.name}</a></li>
	</ul>
	<div id="tab{$ui.id}">{import file="UIBizSystem.st"}</div>
</div>
<script type='text/javascript'>
bizmodule.tabs({
	moduleID: '{$moduleID}',
	moduleName: '{$ui.name}',
	uiid: '{$ui.uiid}',
	tabTemplate: "{$tabTemplate}",
    cache: true
});
</script>
