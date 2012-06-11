<form id="{$ui.uiid}" class="bzsys bzsys_{$mode}" method="POST" action="{$submitUrl}" target="_blank">
	<input type="hidden" name="fixedNaviRules" value="{$fixedNaviRules}">
    <input type="hidden" name="naviRules" value="{$naviRules}">
    <input type="hidden" name="sidx" value="{$sidx}">
	<input type="hidden" name="sord" value="{$sord}">
	<div class="bzgrp">
		{foreach from=$ui.groups item="grp"} {foreach from=$grp.children item="fld" key="key"} 
		<span><input type="checkbox" id="fld{$fld.id}"
			name="fields" value="{$fld.propName}" /><label for="fld{$fld.id}">{$fld.label}</label>&nbsp;&nbsp;
		</span> {/foreach} {/foreach}
	</div>
	<div class="bzsys_btns">
		<input type="submit" value="提交" class="submitButton" /> <input type="submit" value="返回" class="returnButton" style="display: none;" />
	</div>
</form>
