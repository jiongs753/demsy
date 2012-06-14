{if $ui.name != null}<div class="bzsys_head">{$ui.name}</div>{/if}
{if $ui.size == 1 || $ui.hasRichText || $ui.layout == 0}
	{foreach from=$ui.groups item="grp"}
		{foreach from=$grp.hiddenFields item="fld"}
			{render ui=$fld uiRender=$uiRender}
		{/foreach}
		{if $grp.mode != "N" && $grp.shownFieldsSize > 0}
        <div class="bzgrp">
            {if $ui.size > 1}<div class="bzgrp_head">{$grp.name}</div>{/if}
		<table border="0" valign="top" cellpadding="0" cellspacing="0" width="100%" >
			{foreach from=$grp.shownFields item="fld" key="key"}
				{if $fld.rowBegin}<tr class="bzrow {if $key==0}bzfirstrow{/if}">{/if}
					{if $fld.shownFieldsSize > 0}
					<td class="bzfld_head" valign="top" style="font-style: italic;text-align:left;" colspan=2>{$fld.label}</td>
					</tr>{render ui=$fld uiRender=$uiRender}
					{else}
					{if $grp.shownFieldsSize > 1}
					<td class="bzfld_head" valign="top">
						{if $fld.mode == "M"}<span class="bzfld_M">&nbsp;</span>{/if}{$fld.label}
					</td>
					{/if}
					<td class="bzfld_data"{if $fld.colSpan>1} colspan='{$fld.colSpan}'{/if}>
						{render ui=$fld uiRender=$uiRender}
						{if $fld.param.desc != null}<span class="bzfld_desc">{$fld.param.desc}</span>{/if}
					</td>
					{/if}
				{if $fld.rowEnd}</tr>{/if}
			{/foreach}
		</table>
        </div>
		{/if}
	{/foreach}
{else}
<div id="system_{$ui.uiid}" class="accordion" >
	{foreach from=$ui.groups item="grp"}
		{foreach from=$grp.hiddenFields item="fld"}
			{render ui=$fld uiRender=$uiRender}
		{/foreach}
		{if $grp.mode != "N" && $grp.shownFieldsSize > 0}
		<div class="menu"><span href="#tab_{$grp.uiid}">{$grp.name}</span></div>
		<div class="content" id="tab_{$grp.uiid}">
			<table class="bzgrp" border="0" valign="top" cellpadding="0" cellspacing="0" width="100%" >{foreach from=$grp.shownFields item="fld" key="key"}
				{if $fld.rowBegin}<tr class="bzrow {if $key==0}bzfirstrow{/if}">{/if}
					{if $fld.shownFieldsSize > 0}
					<td class="bzfld_head" style="font-style: italic;text-align:left;" colspan=2>{$fld.label}</td>
					</tr>{render ui=$fld uiRender=$uiRender}
					{else}
					{if $grp.shownFieldsSize > 1}
					<td class="bzfld_head">{if $fld.mode == "M"}<span class="bzfld_M">&nbsp;</span>{/if}{$fld.label}</td>
					<td class="bzfld_data"{if $fld.colSpan>1} colspan='{$fld.colSpan}'{/if}>{render ui=$fld uiRender=$uiRender}</td>
					{else}
					<td style="border: 1px solid transparent;" {if $fld.colSpan>1} colspan='{$fld.colSpan}'{/if}>{render ui=$fld uiRender=$uiRender}</td>
					{/if}
					{/if}
				{if $fld.rowEnd}</tr>{/if}
			{/foreach}</table>
		</div>{/if}
	{/foreach}
</div>
<script>
$("#system_{$ui.uiid}").accordion();
</script>
{/if}