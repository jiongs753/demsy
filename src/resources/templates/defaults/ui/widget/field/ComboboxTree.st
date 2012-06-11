{tostring bean=$data prop=$ui.propName pattern=$ui.pattern assign="value"}

<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
<button type="button" class="Combobox ComboboxTree ui-widget ui-widget-content"{if $ui.comboboxUrl != null} url="{$ui.comboboxUrl}"{/if}>
	<div class="label"></div>
	<span class="ui-icon ui-icon-triangle-1-s"></span>
</button>
