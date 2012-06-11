{if $block.cellCount == null || $block.cellCount == 0} 
<table width='100%' border="0" cellpadding="0" cellspacing="0">
	{foreach from=$data.items item="item" key="idx"}
    <tr class="i">
		<td class="il b{$idx}"></td>
        {foreach from=$blockParser.fields item="field"}
        <td class="{$field}">{tostring bean=$item.obj prop=$field}</td>
        {/foreach}
        <td class="ir"></td>
	</tr>
    {/foreach}
</table>
{else}
<table width='100%' border="0" cellpadding="0" cellspacing="0">
    {foreach from=$data.items item="row"}
        <tr class="i">
    	{foreach from=$row.items item="item" key="idx"}
    		<td class="il b{$idx}"></td>
            {foreach from=$blockParser.fields item="field"}
            <td class="{$field}">{tostring bean=$item.obj prop=$field}</td>
            {/foreach}
            <td class="ir"></td>
        {/foreach}
        </tr>
    {/foreach}
</table>
{/if}