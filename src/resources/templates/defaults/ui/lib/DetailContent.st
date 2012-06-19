<script src="{$scriptPath}/demsy.xheditor.js" type="text/javascript"></script>
<div class="t"></div>
<div class="d">
	<div class="name">{$obj.name}</div>
	<div class="info">
		{if $obj.infoOrigin!=null}<span>来源：{$obj.infoOrigin}</span>&nbsp;&nbsp;{/if} 
		{if $obj.infoAuthor!=null}<span>作者：{$obj.infoAuthor}</span>&nbsp;&nbsp;{/if}
        <span>日期：{if $obj.infoDate !=null}{$obj.infoDate}{else}{tostring bean=$obj prop="created" pattern="yyyy-MM-dd HH:mm"}{/if}</span>
	</div>
	<div class="cont">
    {if $obj.content != null}
        {$obj.content}
    {else}
        <div class="img">
        {if $obj.image != null}{import file="img.st" img=$obj.image}{/if}
        </div>
        <p>
        {if $obj.desc !=null}{tohtml bean=$obj.desc}{/if}
        </p>
    {/if}


    <div style="margin-top: 20px; text-align: right;" class="info2">
        {if $obj.createdBy!=null}<span>编辑：{$obj.createdBy}</span>&nbsp;&nbsp;<span>浏览：{$obj.clickNum} 次</span>{/if}
    </div>
    {import file="_share.st"}
    </div>
	<div class="cmt">
		<table class="cmtinfo" style="text-align: left;">
            <tr>
			<td class="cmtinfo1">我要评论</td>
			{if $obj.commentNum != null}<td class="cmtinfo2"><a href='javascript:ui.gotoPage("blockcomment{$block.id}","commentPage=1&pageSize=10");'>已有&nbsp;<span style="color: red;">{$obj.commentNum}</span>&nbsp;人参与评论</a></td>{/if}
			</tr>
		</table>
		<div class="cmtform">
		<form id="form_{$ui.uiid}" action="{$commentUrl}">
			<input type="hidden" name="data.module.id" value="{$module.id}" />
			<input type="hidden" name="data.subjectID" value="{$obj.id}" />
			<textarea style="height: 200px; width: 100%;" id="textarea_{$ui.uiid}" class="xheditor" name="data.content"></textarea>
			<div style="text-align: right;padding: 2px;"><input type="submit" class="submitButton" value="提交评论" /></div>
		</form>
		</div>
        <div class="cmtlist" id="blockcomment{$block.id}" dataID="{$block.id}">
		</div>
	</div>
</div>
<div class="b"></div>
<script type="text/javascript">
$(function() {
	var $form=$("#form_{$ui.uiid}");
	$(".submitButton", $form).click(function() {
		$form.block(); 
        var form=$form.get(0);
		$.post(form.action, $form.serialize(), function(json) {
            alert(json.message);
            if(json.success){
                form.reset();
            	ui.gotoPage("blockcomment{$block.id}","commentPage={if $pager.totalPage == null}1{else}{$pager.totalPage}{/if}");
            }
		}, "json");

		return false;
	});
});
function gotoPage{$block.id}(){
	if(ui){
		ui.gotoPage("blockcomment{$block.id}","commentPage=1&pageSize=10");
	}else{
		setTimeout("gotoPage{$block.id}()",200);
	}
}
gotoPage{$block.id}();
</script>