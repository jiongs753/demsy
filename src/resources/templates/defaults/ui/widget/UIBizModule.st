<script src="{$scriptPath}/jquery.ui.tabs.js" type="text/javascript"></script>
<link href="{$stylePath}/jquery.ui.tabs.css" rel="stylesheet" type="text/css" media="screen" />
<script src="{$scriptPath}/demsy.bizmodule.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.popup.js" type="text/javascript"></script>
<link href="{$stylePath}/demsy.bizform.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery-jvert-tabs.css" rel="stylesheet" type="text/css" media="screen" />
<style>
.main_bg{
	background-image: url({$contextPath}/themes2/images/index_main_bg.jpg);
	background-repeat: repeat;
	background-position: left top;
}
</style>
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
<script src="{$scriptPath}/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="{$scriptPath}/ckfinder/ckfinder.js" type="text/javascript"></script>
