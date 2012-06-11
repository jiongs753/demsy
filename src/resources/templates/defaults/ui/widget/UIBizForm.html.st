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
<div id="tabs_{$ui.uiid}" style="visibility: hidden;">
	<div>
		<ul>{foreach from=$ui.groups item="grp"}
			{if $grp.mode != "N" && $grp.shownFieldsSize > 0}
			<li><a href="#tab_{$grp.uiid}">{$grp.name}</a></li>{/if}
			{/foreach}			
		</ul>
	</div>
	<div>
	{foreach from=$ui.groups item="grp"}
		{foreach from=$grp.hiddenFields item="fld"}
			{render ui=$fld uiRender=$uiRender}
		{/foreach}
		{if $grp.mode != "N" && $grp.shownFieldsSize > 0}
		<div id="tab_{$grp.uiid}" style="display:none;">
		<table class="bzgrp" border="0" valign="top" cellpadding="0" cellspacing="0" width="100%" >
			{foreach from=$grp.shownFields item="fld" key="key"}
				{if $fld.rowBegin}<tr class="bzrow {if $key==0}bzfirstrow{/if}">{/if}
					{if $fld.shownFieldsSize > 0}
					<td class="bzfld_head" style="font-style: italic;text-align:left;" colspan=2>{$fld.label}</td>
					</tr>{render ui=$fld uiRender=$uiRender}
					{else}
					{if $grp.shownFieldsSize > 1}
					<td class="bzfld_head">{if $fld.mode == "M"}<span class="bzfld_M">&nbsp;</span>{/if}{$fld.label}</td>
					{/if}
					<td class="bzfld_data"{if $fld.colSpan>1} colspan='{$fld.colSpan}'{/if}>{render ui=$fld uiRender=$uiRender}</td>
					{/if}
				{if $fld.rowEnd}</tr>{/if}
			{/foreach}
		</table>
		</div>{/if}
	{/foreach}
	</div>
</div>
<script>
$("#tabs_{$ui.uiid}").jVertTabs().css("visibility", "visible");
</script>
{/if}