 {if $block.naviButton && $pager.totalPage > 1}
<div class="pager" style="cursor: pointer;">
    {if $pager.pageIndex != 1}
    <a class="first navi" onclick='ui.gotoPage("block{$block.id}","page=1");'><span>第一页</span></a>
    &nbsp;<a class="prev navi" onclick='ui.gotoPage("block{$block.id}","page={$pager.prePage}");'><span>上一页</span></a>
    {/if}
    {if $pager.pageIndex != $pager.totalPage}
    &nbsp;<a class="next navi" onclick='ui.gotoPage("block{$block.id}","page={$pager.nextPage}");'><span>下一页</span></a>
    &nbsp;<a class="last navi" onclick='ui.gotoPage("block{$block.id}","page={$pager.totalPage}");'><span>最后页</span></a>
    {/if}
    {if $pager.pageIndex != 1 || $pager.pageIndex != $pager.totalPage}
    <span class="msg">&nbsp;当前显示({$pager.fromRecord} - {$pager.toRecord})/{$pager.totalRecord}条，{$pager.pageIndex}/{$pager.totalPage}页</span>
    {/if}
</div>
{/if}
