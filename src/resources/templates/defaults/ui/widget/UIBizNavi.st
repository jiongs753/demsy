<div id="{$ui.uiid}"></div>
<script type="text/javascript">
bizmodule.addNaviMenu({
	uiid: "{$ui.uiid}",
	moduleID: "{$moduleID}",{if $comboboxID != null}
	comboboxID: "{$comboboxID}",
	comboboxType: "{$comboboxType}",{/if}
	showcheck: true,
	cbiconpath: '{$imagePath}/tree',{if $data != null}
	data: {import file="BizNavi.json.st" data=$data},{else}
	url: '{$ui.dataUrl}',{/if}
	complete: false
});
</script>
