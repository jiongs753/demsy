<dl>
	<dd class="d">
		<ul>{foreach from=$data.items item="item" key="idx"}
			<li class="i">
				<div class="il row{$idx}" style="float: left;"></div>
				<div class="it">{import file="a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</div>
			</li>{/foreach}
		</ul>
		{if $block.naviButton && $pager.totalPage > 1}
		<div class="pager">
			{if $pager.pageIndex != 1}
			<a class="first navi" onclick='ui.gotoPage("block{$block.id}","page=1","{if $dataParam != null}{$dataParam}{/if}");'><span>第一页</span></a>
			&nbsp;<a class="prev navi" onclick='ui.gotoPage("block{$block.id}","page={$pager.prePage}","{if $dataParam != null}{$dataParam}{/if}");'><span>上一页</span></a>
			{/if}
			{if $pager.pageIndex != $pager.totalPage}
			&nbsp;<a class="next navi" onclick='ui.gotoPage("block{$block.id}","page={$pager.nextPage}","{if $dataParam != null}{$dataParam}{/if}");'><span>下一页</span></a> 
			&nbsp;<a class="last navi" onclick='ui.gotoPage("block{$block.id}","page={$pager.totalPage}","{if $dataParam != null}{$dataParam}{/if}");'><span>最后页</span></a>
			{/if} 
		</div>
		{/if}
	</dd>
</dl>