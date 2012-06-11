{prop bean=$data name=$ui.propName assign="value"}
{key bean=$value assign="fieldValue"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E" || $mode == "R" || $mode == "D"}
<div id="combGrid{$ui.id}" class="Combobox ComboboxSys" {if $ui.comboboxUrl !=null} url="{$ui.comboboxUrl}"{/if}>
	<input id="{$ui.uiid}" class="value{if $cascade != null} cascade{/if} " name="{$ui.name}" value="{$fieldValue}" type="hidden"/>
	<input name="{$ui.uiid}_label" class="label input" value="{import file="../../../common/optionstr.st" keyProp=$ui.keyProp nodes=$ui.optionNode.children}"  type="text" readonly="true" />
	<div class="ui-icon ui-icon-triangle-1-s"></div>
</div>
{elseif $mode == "I"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value.id}" type="hidden" />
<span>{import file="../../../common/optionstr.st" nodes=$ui.optionNode.children}</span>
{elseif $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value.id}" type="hidden" />
{elseif $mode == "S"}
<span>{import file="../../../common/optionstr.st" nodes=$ui.optionNode.children}</span>
{elseif $mode == "P" && $value.id != null && $value.id > 0}
<span>{import file="../../../common/optionstr.st" nodes=$ui.optionNode.children}</span>
{/if}
