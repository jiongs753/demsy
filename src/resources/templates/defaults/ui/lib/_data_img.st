{if $block.cellCount == null || $block.cellCount == 0} {if $block.horizontal}
<table width='100%' border="0" cellpadding="0" cellspacing="0">
	<tr>
		{foreach from=$data.items item="item"}
		<td class="i">{import file="a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td>{/foreach}
	</tr>
</table>
{else}
<ul>
	{foreach from=$data.items item="item"}
	<li class="i">{import file="a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</li>{/foreach}
</ul>
{/if} {else}
<table width='100%' border="0" cellpadding="0" cellspacing="0">
	{foreach from=$data.items item="row"}
	<tr>
		{foreach from=$row.items item="item"}
		<td class="i">{import file="a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td> {/foreach}
	</tr>
	{/foreach}
</table>
{/if}
