<script src="{$scriptPath}/demsy.xheditor.js" type="text/javascript"></script>
<div class="t"></div>
<div class="d">
    <div class="buttons">
        <a href="#bottom" style="align: right;"><img src='{$contextPath}/themes2/images/bbs/images/newthread.gif' /></a>
    </div>
    <table width='100%' align=center cellpadding=0 cellspacing=0 border=0>
        <tr class='header'>
            <th colspan=2>状态</th>
            <th>【{$data.name}】标题</th>
            <th>网友</th>
            <th>时间</th>
            <th>回复/点击</th>
        </tr>
        {foreach from=$data.items item="item"} {assign var="obj" value=$item.obj} {if $obj != null}
        <tr class='item'>
            <td class="posticon" width="20px">{if $obj.posticon != null}<img src='{$contextPath}/themes2/images/bbs/images/{$obj.posticon}' />{/if}</td>
            <td class="titleicon" width="20px">{if $obj.catalog != null}<img width="22px" src='{$contextPath}/themes2/images/bbs/posticons/{$obj.catalog}' />{/if}</td>
            <td class="title"><a href="{$item.href}" {if $target!=null} target="{$item.target}"{/if}>{tohtml bean=$obj prop="name"}</a></td>
            <td class="author">{if $obj.createdBy == null || $obj.createdBy == "客人" || $obj.createdBy == "匿名网友"}来自: {ipname bean=$obj.createdIP}{else}{tohtml bean=$obj prop="createdBy"}{/if}</td>
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
        <form id="form_{$ui.uiid}" action="" style="text-align: right;" onsubmit="return false;">
            <input type="hidden" name="data.forum.id" value="{$data.obj.id}" />
            <table width='100%' align=center cellpadding=0 cellspacing=0 border=0>
                <tr class='header'>
                    <th colspan=2>【{$data.name}】>> 发布新帖子</th>
                </tr>
                <tr>
                    <td class="title">帖子分类</td>
                    <td align="left"><input type="radio" name="data.catalog" value="" checked="true" /> <input type="radio" name="data.catalog" id="em01" value="em01.gif" /><label for="em01"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em01.gif" /> </label> <input type="radio" name="data.catalog" id="em02" value="em02.gif" /><label for="em02"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em02.gif" /> </label> <input type="radio" name="data.catalog" id="em03" value="em03.gif" /><label for="em03"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em03.gif" /> </label> <input type="radio" name="data.catalog" id="em04" value="em04.gif" /><label for="em04"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em04.gif" /> </label> <input type="radio" name="data.catalog" id="em05" value="em05.gif" /><label for="em05"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em05.gif" /> </label> <input type="radio" name="data.catalog" id="em06" value="em06.gif" /><label for="em06"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em06.gif" /> </label> <input type="radio" name="data.catalog" id="em07" value="em07.gif" /><label for="em07"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em07.gif" /> </label> <input type="radio" name="data.catalog" id="em08" value="em08.gif" /><label for="em08"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em08.gif" /> </label> <input type="radio" name="data.catalog" id="em09" value="em09.gif" /><label for="em09"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em09.gif" /> </label> <input type="radio" name="data.catalog" id="em10" value="em10.gif" /><label for="em10"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em10.gif" /> </label> <input type="radio" name="data.catalog" id="em11" value="em11.gif" /><label for="em11"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em11.gif" /> </label> <input type="radio" name="data.catalog" id="em12" value="em12.gif" /><label for="em12"><img
                            src="{$contextPath}/themes2/images/bbs/posticons/em12.gif" /> </label>
                    </td>
                </tr>
                <tr>
                    <td class="title">帖子标题</td>
                    <td align="left"><input type="text" class="input" name="data.name" style="width: 50%;" /></td>
                </tr>
                <tr>
                    <td class="title">帖子内容</td>
                    <td align="left"><textarea style="width: 99%; height: 200px;" id="textarea_{$ui.uiid}" name="data.content"></textarea></td>
                </tr>
                <tr>
                    <td class="title">查看方式</td>
                    <td align="left"><select class="input" name="data.viewMode" style="width: 200px" onchange="
                    	if(this.value=='4'){
                    		$('#bbsviewusers').show();
                    	}else{
                    		$('#bbsviewusers').hide();
                    	}" >
                    	<option value="0">允许所有用户查看该帖</option>
                    	<option value="1">允许登录用户查看该帖</option>
                    	<option value="3">只有版主可以查看该帖</option>
                    	<option value="4">允许指定用户查看该帖</option>
                    </select>
                    </td>
                </tr>
                <tr id="bbsviewusers"  style="display:none;">
                    <td class="title">指定用户</td>
                    <td align="left"><input type="text" class="input" name="data.viewUsers" style="width: 50%;" />(注：填写逗号分隔的帐号列表)</td>
                </tr>
            </table>
            <input type="submit" class="submitButton" value="" />
        </form>
    </div>
</div>
<div class="b"></div>
<script type="text/javascript">
$(function() {
    var $form=$("#form_{$ui.uiid}");
    //var $richtext=$("#textarea_{$ui.uiid}").cleditor({
      //  width: "99%",
        //height: "200px"
    //});
    $('#textarea_{$ui.uiid}').xheditor({
    	{if $login != null}
        upLinkUrl:"{$uploadUrl}",
        upLinkExt:"zip,rar,txt",
        upImgUrl:"{$uploadUrl}",
        upImgExt:"jpg,jpeg,gif,png",
        upFlashUrl:"{$uploadUrl}",
        upFlashExt:"swf",
        upMediaUrl:"{$uploadUrl}",
        upMediaExt:"wmv,avi,wma,mp3,mid",
        {if $login.roleType < 90}
        tools:'full'
        {/if}
        {else}
        tools:'simple'
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

