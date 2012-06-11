{tostring bean=$data prop=$ui.propName assign="value"}
{tostring bean=$data prop=$ui.propName2 assign="value2"}

<div id="combGrid{$ui.id}" class="Combobox ComboboxSys" {if $ui.comboboxUrl !=null} url="{$ui.comboboxUrl}"{/if}>
	<input class="value" name="{$ui.name}" type="hidden" value="{$value}" />
	<input class="label input" name="{$ui.name2}" type="text" value='{$value2}' readonly="true" />
	<div class="ui-icon ui-icon-triangle-1-s"></div>
</div>
