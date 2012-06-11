{if $block.cellCount == null || $block.cellCount == 0} {if $block.horizontal}
<table border="0" cellpadding="0" cellspacing="0" valign="top">
	<tr>
		{foreach from=$data.items item="item"}
		<td class="i" valign="top">
			<table border="0" cellpadding="0" cellspacing="0" valign="top">
				<tr>
					<td class="il" valign="top">{import file="a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td>
					<td class="it" valign="top">{import file="a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</td>
				</tr>
				<tr>
					<td class="desc" colspan=2 valign="top"><div>{tohtml bean=$item.obj.desc}</div></td>
				</tr>
			</table>
		</td>{/foreach}
	</tr>
</table>
{else}
<ul>
	{foreach from=$data.items item="item"}
	<li class="i">
		<table border="0" cellpadding="0" cellspacing="0" valign="top">
				<tr>
					<td class="il" valign="top">{import file="a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td>
					<td class="it" valign="top">{import file="a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</td>
				</tr>
				<tr>
					<td class="desc" colspan=2 valign="top"><div>{tohtml bean=$item.obj.desc}</div></td>
				</tr>
		</table>
	</li>{/foreach}
</ul>
{/if} {else}
<table border="0" cellpadding="0" cellspacing="0">
	{foreach from=$data.items item="row"}
	<tr>
		{foreach from=$row.items item="item"}
		<td class="i">
			<table border="0" cellpadding="0" cellspacing="0" valign="top">
				<tr>
					<td class="il" valign="top">{import file="a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td>
					<td class="it" valign="top">{import file="a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</td>
				</tr>
				<tr>
					<td class="desc" colspan=2 valign="top"><div>{tohtml bean=$item.obj.desc}</div></td>
				</tr>
			</table>
		</td> {/foreach}
	</tr>
	{/foreach}
</table>
{/if}
