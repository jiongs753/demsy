<script src="{$scriptPath}/demsy.xheditor.js" type="text/javascript"></script>
<div class="t"></div>
<div class="d">
    <div class="buttons">
        <a href="#bottom"><img src='{$contextPath}/themes2/images/bbs/images/newthread.gif' /></a>
    </div>
    <table width='100%' align=center cellpadding=0 cellspacing=0 border=0>
        <tr class='header'>
            <th>标题</th>
            <th>网友</th>
            <th>时间</th>
            <th>回复/点击</th>
        </tr>
        {foreach from=$data.items item="item"} {assign var="obj" value=$item.obj} {if $obj != null}
        <tr class='item'>
            <td class="title"><a href="{$item.href}" {if $target!=null} target="{$item.target}"{/if}>{tohtml bean=$obj prop="name"}</a></td>
            <td class="author">{tohtml bean=$obj prop="createdBy"}</td>
            <td class="postattime" width="150px">{tostring bean=$obj prop="created" pattern="yyyy-MM-dd HH:mm"}</td>
            <td class="reply" width="80px">{tostring bean=$obj prop="commentNum"} / {tostring bean=$obj prop="clickNum"}</td>
        </tr>
        {/if} {/foreach}
        <tr class=footer>
            <td colspan=7><span class="msg">&nbsp;共 {$pager.totalPage} 页 {$pager.totalRecord} 条记录，当前显示第 {$pager.pageIndex} 页 </span> {if $pager.totalPage > 0}{if $pager.pageIndex != 1} <a
                href='javascript: ui.gotoPage("block{$block.id}","page=1");' title="第一页">第一页</a>&nbsp; <a href='javascript: ui.gotoPage("block{$block.id}","page={$pager.prePage}");' title="上一页">上一页</a>&nbsp;
                {/if} {if $pager.pageIndex != $pager.totalPage} <a href='javascript: ui.gotoPage("block{$block.id}","page={$pager.nextPage}");' title="下一页">下一页</a>&nbsp; <a
                href='javascript: ui.gotoPage("block{$block.id}","page={$pager.totalPage}");' title="最后页">最后页</a>&nbsp; {/if}{/if}</td>
        </tr>
    </table>
    <a id="bottom"></a>
    <div class="topicform">
        {if $login != null}
        <form id="form_{$ui.uiid}" action="" style="text-align: right;" onsubmit="return false;">
            <input type="hidden" name="data.forum.id" value="{$data.obj.id}" />
            <table width='100%' align=center cellpadding=0 cellspacing=0 border=0>
                <tr class='header'>
                    <th colspan=2>发表博文</th>
                </tr>
                <tr>
                    <td class="title">博文标题</td>
                    <td align="left"><input type="text" class="input" name="data.name" style="width: 50%;" /></td>
                </tr>
                <tr>
                    <td class="title">博文摘要</td>
                    <td align="left"><textarea style="width: 99%; height: 50px;" name="data.desc"></textarea></td>
                </tr>
                <tr>
                    <td class="title">博文内容</td>
                    <td align="left"><textarea style="width: 99%; height: 200px;" readonly=true, id="textarea_{$ui.uiid}" name="data.content"></textarea></td>
                </tr>
            </table>
            <input type="submit" class="submitButton" value="" />
        </form>
        {/if}
    </div>
</div>
<div class="b"></div>
<script type="text/javascript">
$(function() {
    var $form=$("#form_{$ui.uiid}");
    $('#textarea_{$ui.uiid}').xheditor({
    	{if $login != null}
        upLinkUrl:"{$uploadUrl}",
        upLinkExt:"zip,rar,txt",
        upImgUrl:"{$uploadUrl}",
        upImgExt:"jpg,jpeg,gif,png",
        upFlashUrl:"{$uploadUrl}",
        upFlashExt:"swf",
        upMediaUrl:"{$uploadUrl}",
        upMediaExt:"wmv,avi,wma,mp3,mid"
        {/if}
    });
    $(".submitButton", $form).click(function() {
        $form.block();
        var form=$form.get(0); 
        $.post("{$topicUrl}", $form.serialize(), function(json) {
            alert(json.message);
            if(json.success){
                form.reset();
            	ui.gotoPage("block{$block.id}","page=1");
            }
        }, "json");

        return false;
    });
});
</script>

