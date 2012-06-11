{if $block.cellCount == null || $block.cellCount == 0} {if $block.horizontal}
<table width='100%' border="0" cellpadding="0" cellspacing="0">
	<tr>
		{foreach from=$data.items item="item" key="idx"}
		<td class="i" style="white-space: nowrap;">
			<div class="il row{$idx}" style="float: left;">&nbsp;</div>
			<div class="it">
                {import file="a.st" href=$item.href target=$item.target title=$item.title name=$item.name}
                {if $item.isnew}<img src="{$themePath}/images/new.gif" />{/if}
            </div>
		</td>{/foreach}
	</tr>
</table>
{else}
<ul>
	{foreach from=$data.items item="item" key="idx"}
	<li class="i">
		<div class="il row{$idx}" style="float: left;">&nbsp;</div>
			<div class="it">
                {import file="a.st" href=$item.href target=$item.target title=$item.title name=$item.name}
                {if $item.isnew}<img src="{$themePath}/images/new.gif" />{/if}
            </div>
	</li>{/foreach}
</ul>
{/if} {else}
<table width='100%' border="0" cellpadding="0" cellspacing="0">
	{foreach from=$data.items item="row"}
	<tr>
		{foreach from=$row.items item="item"}
		<td class="i" style="white-space: nowrap;">
			<div class="il row{$idx}" style="float: left;">&nbsp;</div>
			<div class="it">
                {import file="a.st" href=$item.href target=$item.target title=$item.title name=$item.name}
                {if $item.isnew}<img src="{$themePath}/images/new.gif" />{/if}
            </div>
		</td>{/foreach}
	</tr>
	{/foreach}
</table>
{/if}
