{foreach from=$nodes item="item"}
	{key bean=$item.params assign="itemValue"}
	{if $item.size > 0}
		{prop bean=$item name="isSelf" assign="isSelf"}
		{if $isSelf == "true"}<option{if $fieldValue == $itemValue} selected{/if} value="{$itemValue}">{$item.name}</option>{/if}
	{else}
		<option{if $fieldValue == $itemValue} selected{/if} value="{$itemValue}">{$item.name}</option>
	{/if}
{/foreach}
{foreach from=$nodes item="item"}
{if $item.size > 0}
<optgroup label="{$item.name}">{import file="options.st" nodes=$item.children}</optgroup>
{/if}
{/foreach}
