<table border="0" valign="top" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td>&nbsp;</td>
		<td valign="top" height=30 style="width: 214px;">
			<button id="navi_{$comboboxID}_comb" type="button" class="Combobox ui-widget-content">
				<div class="label">数据分类</div>
				<span class="ui-icon ui-icon-triangle-1-s"></span>
			</button>
		</td>
	</tr>
</table>
{render ui=$ui.grid uiRender=$uiRender search="false" comboboxID=$comboboxID}
<div id="navi_{$comboboxID}" style="width: 214px; height: 400px;">{render ui=$ui.naviMenu uiRender=$uiRender comboboxID=$comboboxID}</div>
<script type="text/javascript">
$("#navi_{$comboboxID}_comb").popup($("#navi_{$comboboxID}"), {
	hide: function(){
		bizmodule.combSys['$comboboxID'].grid.trigger("reloadGrid");
	}
});
</script>
