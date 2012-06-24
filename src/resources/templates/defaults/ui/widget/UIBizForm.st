{if $importJS != null}
<script src="{$scriptPath}/demsy.bizmodule.js" type="text/javascript"></script>
<link href="{$stylePath}/demsy.bizform.css" rel="stylesheet" type="text/css" media="screen" />
<link href="{$stylePath}/jquery.ui.spinner.css" rel="stylesheet" type="text/css" media="screen" />
<script src="{$scriptPath}/jquery.ui.spinner.js" type="text/javascript"></script>

{if $ui.size == 1 || $ui.hasRichText}
{else}
<script src="{$scriptPath}/accordion/demsy.accordion.js" type="text/javascript"></script>
<link href="{$scriptPath}/accordion/demsy.accordion.css" rel="stylesheet" type="text/css" media="screen" />
{/if}
{if $ui.hasCombobox}
<script src="{$scriptPath}/jquery.popup.js" type="text/javascript"></script>
{/if}
{if $ui.hasMultiSelect}
<script src="{$scriptPath}/demsy.dropdownselect.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.dropdownselect-zh-CN.js" type="text/javascript"></script>
<link href="{$stylePath}/jquery.dropdownselect.css" rel="stylesheet" type="text/css" media="screen" />
{/if}
{if $ui.hasDate}
<script src="{$scriptPath}/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<link href="{$stylePath}/jquery.ui.datepicker.css" rel="stylesheet" type="text/css" media="screen" />
{/if}
{if $ui.hasUpload}
<script src="{$scriptPath}/demsy.uploadify.js" type="text/javascript"></script>
<script src="{$scriptPath}/swfobject.js" type="text/javascript"></script>
<link href="{$stylePath}/jquery.uploadify.css" rel="stylesheet" type="text/css" media="screen" />
{/if}
{if $ui.hasRichText}
<script src="{$scriptPath}/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="{$scriptPath}/ckfinder/ckfinder.js" type="text/javascript"></script>
{/if}
{if $ui.hasColor}
<script src="{$scriptPath}/demsy.colorpicker.js" type="text/javascript"></script>
<link href="{$stylePath}/demsy.colorpicker.css" rel="stylesheet" type="text/css" media="screen" />
{/if}
{/if}

<form id="{$ui.uiid}" class="bzsys bzsys_{$mode}" method="POST" onsubmit="return false;">
	<div class="bzgrps">
		{import file="UIBizForm.html.st"}
	</div>
	{if $dialog == null}
		<div class="bzsys_btns">
		  <input type="submit" value="提交" class="submitButton" style="display: none;" />
		  <input type="submit" value="返回" class="returnButton" style="display: none;" />
		</div>
	{/if}
</form>
<script type="text/javascript">	
bizmodule.form({
	uiid: '{$ui.uiid}',
	moduleID: '{$moduleID}',
	actionID: '{$actionID}',{if $submitUrl != null}
	submitUrl: '{$submitUrl}',{/if}
	reloadUrl: "{$reloadUrl}",
	minWidth: 200,
	contextPath: '{$contextPath}'
});
</script>
