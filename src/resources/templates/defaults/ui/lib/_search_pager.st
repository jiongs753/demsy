 {if $block.naviButton && $pager.totalPage > 0}
<div class="pager">
    {if $pager.pageIndex != 1}
    <a class="first navi" onclick='ui.gotoPage("block{$block.id}","page=1&keywords={encodeUri bean=$keywords}");'>
        <span>|&lt;&lt;</span>&nbsp;
	</a>
    <a class="prev navi" onclick='ui.gotoPage("block{$block.id}","page={$pager.prePage}&keywords={encodeUri bean=$keywords}");'>
        <span>&lt;&lt;</span>&nbsp;
    </a>
    {/if}
    {if $pager.pageIndex != $pager.totalPage}
    <a class="next navi" onclick='ui.gotoPage("block{$block.id}","page={$pager.nextPage}&keywords={encodeUri bean=$keywords}");'>
        &nbsp;<span>&gt;&gt;</span>
    </a>
    <a class="last navi" onclick='ui.gotoPage("block{$block.id}","page={$pager.totalPage}&keywords={encodeUri bean=$keywords}");'>
        &nbsp;<span>&gt;&gt;|</span>
    </a>
    {/if}
    <span class="msg">&nbsp;当前显示 {$pager.pageIndex}/{$pager.totalPage}页</span>
</div>
{/if}
