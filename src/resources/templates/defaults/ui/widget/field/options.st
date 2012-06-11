{foreach from=$nodes item="item"}
	{if $item.type == "folder" && $item.size > 0}
		{prop bean=$item name="isSelf" assign="isSelf"}
		{if $isSelf == "true"}<option{if $optionValue == $item.id} selected{/if} value="{$item.id}">{$item.name}</option>{/if}
	{else}
		<option{if $optionValue == $item.id} selected{/if} value="{$item.id}">{$item.name}</option>
	{/if}
{/foreach}
{foreach from=$nodes item="item"}
{if $item.type == "folder" && $item.size > 0}
<optgroup label="{$item.name}">{import file="options.st" nodes=$item.children}</optgroup>
{/if}
{/foreach}
