{if $comboboxType == "fk" || $comboboxType == "expr"}
<table border="0" valign="top" cellpadding="0" cellspacing="0" width="650px">
	<tr>
		{if $ui.naviMenu != null}
		<td valign="top" width="188px" height="300px">
			<div class="ui-state-default" style="height: 20px; width: 180px; padding: 5px 0 0 5px;">数据分类</div>
			<div class="ui-widget-content" style="position: relative; padding: 1px; overflow: auto; height: 321px; width: 183px; top: -1px;">
			{render ui=$ui.naviMenu uiRender=$uiRender comboboxType=$comboboxType comboboxID=$comboboxID}
			</div>
		</td>{/if}
		<td width="1px">&nbsp;</td>
		<td valign="top">
			{render ui=$ui.grid uiRender=$uiRender comboboxType=$comboboxType comboboxID=$comboboxID height=300}
		</td>
	</tr>
</table>
{else}
<table border="0" valign="top" cellpadding="0" cellspacing="0">
	<tr>
		<td valign="top">
			<div style="position: relative; overflow: auto; height: 350px; width: 220px;">
			{render ui=$ui.naviMenu uiRender=$uiRender comboboxType=$comboboxType comboboxID=$comboboxID}
			</div>
		</td>
	</tr>
</table>
{/if}