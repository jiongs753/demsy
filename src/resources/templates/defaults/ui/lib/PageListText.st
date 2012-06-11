<dl>
	<dt>
	{import file="Title.st"}
	</dt>
	<dd class="d">
		<div class="result">
			{if $block.scrollDirection == null} {import file="_data_txt.st"} {else}
			<div id="marquee_{$ui.uiid}" class="marquee" direction="{$block.scrollDirection}" style="width: {$">
				{if $block.scrollDirection == "left"}
				<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="marquee1">{import file="_data_txt.st"}</td>
						<td class="marquee2"></td>
					</tr>
				</table>
				{else}
				<div class="marquee1">{import file="_data_txt.st"}</div>
				<div class="marquee2"></div>
				{/if}
			</div>
			{/if}
			<div class="pager">
			{if $pager.totalPage > 0}
				{if $pager.pageIndex != 1}
				<a href='javascript: ui.gotoPage("block{$block.id}","page=1");' title="第一页">第一页</a>&nbsp;
				<a href='javascript: ui.gotoPage("block{$block.id}","page={$pager.prePage}");' title="上一页">上一页</a>&nbsp;
				{/if}
				{if $pager.pageIndex != $pager.totalPage}
				<a href='javascript: ui.gotoPage("block{$block.id}","page={$pager.nextPage}");' title="下一页">下一页</a>&nbsp;
				<a href='javascript: ui.gotoPage("block{$block.id}","page={$pager.totalPage}");' title="最后页">最后页</a>&nbsp;
				{/if}
			{/if}
				<span class="msg">&nbsp;当前显示({$pager.fromRecord} - {$pager.toRecord})/{$pager.totalRecord}条，{$pager.pageIndex}/{$pager.totalPage}页</span>
			</div>
		</div>
	</dd>
	<dd class="b"></dd>
</dl>
