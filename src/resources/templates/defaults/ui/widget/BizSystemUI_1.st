<table border="0" valign="top" cellpadding="0" cellspacing="0" width="100%" style='table-layout: fixed'>
	<tr>
		{if $ui.naviMenu != null}
		<td valign="top" width="228px" height="562px">
			<div class="ui-state-default" style="height: 21px; width: 220px; padding: 6px 0 0 6px;">数据分类</div>
			<div class="ui-widget-content" style="position: relative; padding: 2px; overflow: auto; height: 531px; width: 222px; top: -1px;">
			{render ui=$ui.naviMenu uiRender=$uiRender}
			</div>
		</td>
		<td valign="top" width="3px">&nbsp;</td> {/if}
		<td valign="top">{render ui=$ui.toolbarMenu uiRender=$uiRender}
			<div style="height: 5px;"></div> {render ui=$ui.grid uiRender=$uiRender}</td>
	</tr>
</table>
