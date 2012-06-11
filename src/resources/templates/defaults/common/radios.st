{foreach from=$nodes item="item"}
	{key bean=$item.params assign="itemValue"}
	<input{if $fieldValue == $itemValue} checked{/if} value="{$itemValue}" type="radio" name="{$name}" id="{$name}_{$itemValue}"{if $cascade != null} class="cascade"{/if} />
	<label for="{$name}_{$itemValue}">{$item.name}</label>
{/foreach}
