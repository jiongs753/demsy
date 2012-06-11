<script src="{$scriptPath}/demsy.xheditor.js" type="text/javascript"></script>
<div class="t"></div>
<div class="d">
    <div class="name">{$obj.name}</div>
    <div class="info">
        <span>作者：{if $obj.author!=null}{$obj.author.name}{else}{$obj.createdBy}{/if}</span>&nbsp;&nbsp; 
        <span>日期：{tostring bean=$obj prop="created" pattern="yyyy-MM-dd HH:mm"}</span>&nbsp;&nbsp;
       {if $obj.clickNum !=null} <span>浏览：{$obj.clickNum} 次</span>{/if}
    </div>
    <div class="cont">
        {$obj.content}
    <!-- JiaThis Button BEGIN -->
<div id="ckepop" style="margin-top: 20px;inline-height: 25px;padding: 5px 0 5px 0">
<span class="jiathis_txt">分享到：</span>
<a class="jiathis_button_qzone">QQ空间</a>
<a class="jiathis_button_tsina">新浪微博</a>
<a class="jiathis_button_renren">人人网</a>
<a class="jiathis_button_kaixin001">开心网</a>
<a class="jiathis_button_tqq">腾讯微博</a>
<a class="jiathis_button_t163">网易微博</a>
<a class="jiathis_button_tsohu">搜狐微博</a>
<a class="jiathis_button_taobao">淘江湖</a>
<a class="jiathis_button_xiaoyou">朋友网</a>
</div>
<script type="text/javascript" >
var jiathis_config={
    data_track_clickback:true,
    hideMore:true
}
</script>
<script type="text/javascript" src="http://v2.jiathis.com/code/jia.js" charset="utf-8"></script>
<!-- JiaThis Button END -->
    </div>
    <div class="cmt">
        <table class="cmtinfo">
            <tr>
                <td class="cmtinfo1" style="float: left">我要评论</td>
                {if $obj.commentNum != null}
                <td class="cmtinfo2"><a href='javascript:ui.gotoPage("blockcomment{$block.id}","commentPage=1&pageSize=10");'>已有&nbsp;<span style="color: red;">{$obj.commentNum}</span>&nbsp;人参与评论</a></td>
                {/if}
            </tr>
        </table>
        <div class="cmtform">
            <form id="form_{$ui.uiid}" action="" style="text-align: right;" onsubmit="return false;">
                <input type="hidden" name="data.post.id" value="{$obj.id}" />
                <textarea style="height: 200px;" id="textarea_{$ui.uiid}" name="data.content"></textarea>
                <div>
                    <input type="submit" class="submitButton" value="" />
                </div>
            </form>
        </div>
        <div class="cmtlist" id="blockcomment{$block.id}" dataID="{$block.id}"></div>
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
        upMediaExt:"wmv,avi,wma,mp3,mid",
        {if $login.roleType < 90}
        tools:'simple'
        {/if}
        {else}
        tools:'simple'
        {/if}
    });
	$(".submitButton", $form).click(function() {
		$form.block(); 
        var form=$form.get(0);
		$.post("{$commentUrl}", $form.serialize(), function(json) {
            alert(json.message);
            if(json.success){
                form.reset();
            	ui.gotoPage("block{$block.id}","page={if $pager.totalPage == null}1{else}{$pager.totalPage}{/if}");
            }
		}, "json");

		return false;
	});
});
</script>
