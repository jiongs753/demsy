{prop bean=$data name=$ui.propName assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E" || $mode == "R" || $mode == "D"}
	{if $ui.optionNode != null}{foreach from=$ui.optionNode.children item="node"}
	{contains from=$value item=$node.params assign="checked"}
	<div title="{$node.name}" class="checkbox"><input type="checkbox" id="fld{$ui.id}_{$node.id}" name="{$ui.name}" value="{$node.id}"{if $checked} checked{/if}{if $mode=="R"} readonly{/if}{if $mode=="D"} disabled{/if} />
	<label for="fld{$ui.id}_{$node.id}">{$node.name}</label></div>
	{/foreach}{/if}	
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
