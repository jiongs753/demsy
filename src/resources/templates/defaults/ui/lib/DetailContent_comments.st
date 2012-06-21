{if $pager.totalPage > 0}
<table width="100%" class="cmttable">
    <tr class="cmthead">
        <th>网友</th>
        <th>内容</th>
        <th>时间</th>
    </tr>
    {foreach from=$pager.result item="item"}
    <tr>
        <td width="100px" nowrap>
        {if $item.createdBy == null}来自: {ipname bean=$item.createdIP}{else}{$item.createdBy}{/if}
        </td>
        <td class="cmtcont">{tohtml bean=$item prop="content"}</td>
        <td nowrap>{tostring bean=$item prop="created" pattern="yyyy-MM-dd"}</td>
    </tr>
    {if $item.replyContent != null && $item.replyContent != ""}
    <tr>
        <td>回复:</td>
        <td class="cmtcont">{tohtml bean=$item prop="replyContent"}</td>
        <td nowrap>{tostring bean=$item prop="updated" pattern="yyyy-MM-dd"}</td>
    </tr>
    {/if} {/foreach}
</table>
{if $pager.totalPage > 1}
<div class="pager">
    {if $pager.pageIndex != 1}
    <a href='javascript: ui.gotoPage("blockcomment{$block.id}","commentPage=1&pageSize={$pager.pageSize}");' title="第一页">第一页</a>&nbsp;
    <a href='javascript: ui.gotoPage("blockcomment{$block.id}","commentPage={$pager.prePage}&pageSize={$pager.pageSize}");' title="上一页">上一页</a>&nbsp;
    {/if}
    {if $pager.pageIndex != $pager.totalPage}
    <a href='javascript: ui.gotoPage("blockcomment{$block.id}","commentPage={$pager.nextPage}&pageSize={$pager.pageSize}");' title="下一页">下一页</a>&nbsp;
    <a href='javascript: ui.gotoPage("blockcomment{$block.id}","commentPage={$pager.totalPage}&pageSize={$pager.pageSize}");' title="最后页">最后页</a>&nbsp;
    {/if}
    <span class="msg">&nbsp;当前显示({$pager.fromRecord} - {$pager.toRecord})/{$pager.totalRecord}条，{$pager.pageIndex}/{$pager.totalPage}页</span>
</div>
{/if}
{else}
{/if}