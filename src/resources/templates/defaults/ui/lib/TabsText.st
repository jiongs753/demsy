<dl class="tabs">
	{foreach from=$data.items item="tabdata"}
	<dt><div>{import file="a.st" href=$tabdata.href target=$tabdata.target title=$tabdata.title name=$tabdata.name}</div></dt>
	<dd class="d" style="width:{$block.position.width}px; height:{$block.position.height}px;overflow: hidden;">
		<ul>
			{foreach from=$tabdata.items item="item"}
			<li class="i" style="white-space: nowrap;">
				<div class="il" style="float: left;">&nbsp;</div>
				<div class="it">{import file="a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</div>
			</li>{/foreach}
		</ul>
	</dd>
	{/foreach}
</dl>
