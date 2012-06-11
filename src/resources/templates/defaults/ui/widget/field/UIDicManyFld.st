{prop bean=$data name=$ui.propName assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E" || $mode == "R" || $mode == "D"}
<select id="fld{$ui.id}" name="{$ui.name}" multiple="true" class="multiselect"{if $mode=="R"} readonly{/if}{if $mode=="D"} disabled{/if}>
{if $ui.optionNode != null}{foreach from=$ui.optionNode.children item="node"}
{if $node.type == "leaf"}
{contains from=$value item=$node.params assign="checked"}
<option{if $checked} selected{/if} value="{$node.id}">{$node.name}</option>{/if}
{/foreach}{/if}
</select>
{elseif $mode == "I"}
	{foreach from=$value item="item"}
	<input id="fld{$ui.id}" name="{$ui.name}" value="{$item.id}" type="hidden" />
	{/foreach}
	<span>{tostring bean=$value}</span>
{elseif $mode == "H"}
	{foreach from=$value item="item"}
	<input id="fld{$ui.id}" name="{$ui.name}" value="{$item.id}" type="hidden" />
	{/foreach}
{elseif $mode == "S" || $mode == "P"}
	<span>{tostring bean=$value}</span>
{/if}
