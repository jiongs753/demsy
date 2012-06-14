{foreach from=$ui.hiddenFields item="fld"}
	{render ui=$fld uiRender=$uiRender}
{/foreach}
{if $ui.mode != "N" && $ui.shownFieldsSize > 0}
	{foreach from=$ui.shownFields item="fld"}
	<tr class="bzfld">
		{if $fld.shownFieldsSize > 0}
		<td class="bzfld_head" style="font-style: italic;text-align:left" colspan=2>{$fld.label}</td>
		</tr>{render ui=$fld uiRender=$uiRender}
		{else}
		<td class="bzfld_head">{if $fld.mode == "M"}<span class="bzfld_M">&nbsp;</span>{/if}{$fld.label}</td>
		<td class="bzfld_data">{render ui=$fld uiRender=$uiRender}</td>
		{/if}
	</tr>
	{/foreach}
{/if}
