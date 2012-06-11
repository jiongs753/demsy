{if $block.cellCount == null || $block.cellCount == 0} 
    {if $block.horizontal}
    <table width='100%' border="0" cellpadding="0" cellspacing="0">
    	<tr>
    		{foreach from=$data item="item" key="idx"}
    		<td class="i" style="white-space: nowrap;">
    			<div class="il row{$idx}" style="float: left;">&nbsp;</div>
    			<div class="it">{import file="a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</div>
    		</td>{/foreach}
    	</tr>
    </table>
    {else}
    <ul>
    	{foreach from=$data item="item" key="idx"}
    	<li class="i">
    		<div class="il row{$idx}" style="float: left;"></div>
    		<div class="it">{import file="a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</div>
    	</li>{/foreach}
    </ul>
    {/if} 
{/if}
